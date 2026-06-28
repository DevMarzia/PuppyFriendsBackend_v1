package com.devmarzia.PuppyFriendBackend.controller;

import com.devmarzia.PuppyFriendBackend.entity.AdoptionRequest;
import com.devmarzia.PuppyFriendBackend.payload.AdoptionRequestDto;
import com.devmarzia.PuppyFriendBackend.service.AdoptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adoptions")
public class AdoptionController {

    @Autowired
    private AdoptionService adoptionService;

    @PostMapping
    public ResponseEntity<?> createAdoptionRequest(
            @Valid @RequestBody AdoptionRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails) { 

        AdoptionRequest created = adoptionService.createRequest(dto, userDetails.getUsername());
        return ResponseEntity.ok("Richiesta inviata con successo! ID: " + created.getId());
    }

    @GetMapping
    public ResponseEntity<List<AdoptionRequest>> getAllRequests() {
        return ResponseEntity.ok(adoptionService.getAllRequests());
    }
}
