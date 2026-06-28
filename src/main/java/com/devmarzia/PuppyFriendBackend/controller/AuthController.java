package com.devmarzia.PuppyFriendBackend.controller;

import com.devmarzia.PuppyFriendBackend.entity.Role;
import com.devmarzia.PuppyFriendBackend.entity.Role.RoleType;
import com.devmarzia.PuppyFriendBackend.entity.User;
import com.devmarzia.PuppyFriendBackend.payload.JWTAuthResponse;
import com.devmarzia.PuppyFriendBackend.payload.LoginDto;
import com.devmarzia.PuppyFriendBackend.payload.RegisterDto;
import com.devmarzia.PuppyFriendBackend.repository.RoleRepository;
import com.devmarzia.PuppyFriendBackend.repository.UserRepository;
import com.devmarzia.PuppyFriendBackend.security.JwtTokenProvider;
import com.devmarzia.PuppyFriendBackend.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    private EmailService emailService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider,
                          EmailService emailService){
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;

        this.emailService = emailService;
    }

    // LOGIN
    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto){

        // Autenticazione tramite Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        // Autenticazione salvata 
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Genera il token
        String token = jwtTokenProvider.generateToken(authentication);

        // Restituisce il token al client
        return ResponseEntity.ok(new JWTAuthResponse(token));
    }

    // REGISTRAZIONE
    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){

        // Controlla se l'email esiste già
        if(userRepository.existsByEmail(registerDto.getEmail())){
            return new ResponseEntity<>("Email già in uso!", HttpStatus.BAD_REQUEST);
        }

        // Crea nuovo utente
        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setRegistrationDate(LocalDate.now()); // Data di registrazione automatica

        // Cripta la password prima di salvarla
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        // Gestione Ruoli
        Set<String> strRoles = registerDto.getRoles();
        Set<Role> roles = new HashSet<>();

        if(strRoles == null || strRoles.isEmpty()){
            // Se non vengono specificati i ruoli, è un ADOPTER di default
            Role userRole = roleRepository.findByRoleType(RoleType.ROLE_ADOPTER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            // Mappa le stringhe "admin", "volunteer" ai ruoli veri del DB
            strRoles.forEach(role -> {
                switch(role.toLowerCase()){
                    case "admin":
                        Role adminRole = roleRepository.findByRoleType(RoleType.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "volunteer":
                        Role volRole = roleRepository.findByRoleType(RoleType.ROLE_VOLUNTEER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(volRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByRoleType(RoleType.ROLE_ADOPTER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        // INVIO EMAIL
        try {
            System.out.println("Tentativo invio mail a: " + registerDto.getEmail());
            emailService.sendWelcomeEmail(registerDto.getEmail(), registerDto.getFirstName());
        } catch (Exception e) {
            // Se la mail fallisce, non blocca la registrazione ma stampa l'errore
            System.err.println("Errore durante l'invio della mail: " + e.getMessage());
        }


        return new ResponseEntity<>("Utente registrato con successo!", HttpStatus.CREATED);
    }

}
