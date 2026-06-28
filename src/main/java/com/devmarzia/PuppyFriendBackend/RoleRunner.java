package com.devmarzia.PuppyFriendBackend;

import com.devmarzia.PuppyFriendBackend.entity.Role;
import com.devmarzia.PuppyFriendBackend.entity.Role.RoleType;
import com.devmarzia.PuppyFriendBackend.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component 
public class RoleRunner implements CommandLineRunner {

    private final RoleRepository roleRepository;

    // Costruttore, Spring ci inietta automaticamente il Repository dei ruoli
    public RoleRunner(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Questo metodo viene eseguito automaticamente appena l'app si avvia.
        System.out.println("RoleRunner avviato: controllo presenza ruoli...");

        // Cicla su tutti i valori (ADMIN, VOLUNTEER, ADOPTER)
        for (RoleType roleType : RoleType.values()) {

            if (roleRepository.findByRoleType(roleType).isEmpty()) {
                // Se non esiste (isEmpty), lo creiamo
                Role newRole = new Role();
                newRole.setRoleType(roleType);

                roleRepository.save(newRole); // Lo salviamo nel DB
                System.out.println("Ruolo inserito nel DB: " + roleType);
            }
        }
    }
}
