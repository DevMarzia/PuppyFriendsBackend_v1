package com.devmarzia.PuppyFriendBackend.service;

import com.devmarzia.PuppyFriendBackend.entity.AdoptionRequest;
import com.devmarzia.PuppyFriendBackend.entity.Animal;
import com.devmarzia.PuppyFriendBackend.entity.User;
import com.devmarzia.PuppyFriendBackend.exception.ResourceNotFoundException;
import com.devmarzia.PuppyFriendBackend.payload.AdoptionRequestDto;
import com.devmarzia.PuppyFriendBackend.repository.AdoptionRequestRepository;
import com.devmarzia.PuppyFriendBackend.repository.AnimalRepository;
import com.devmarzia.PuppyFriendBackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdoptionService {

    @Autowired
    private AdoptionRequestRepository adoptionRequestRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private UserRepository userRepository;

    public AdoptionRequest createRequest(AdoptionRequestDto dto, String email) {
        // Trova l'animale
        Animal animal = animalRepository.findById(dto.getAnimalId())
                .orElseThrow(() -> new ResourceNotFoundException("Animale non trovato"));

        // Trova l'utente (dall'email del token)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utente non trovato"));

        // Crea la richiesta
        AdoptionRequest request = new AdoptionRequest();
        request.setAnimal(animal);
        request.setUser(user);
        request.setNotes(dto.getNotes());
        request.setStatus("PENDING"); // Stato iniziale

        return adoptionRequestRepository.save(request);
    }

    // Metodo per vedere tutte le richieste (per l'admin)
    public List<AdoptionRequest> getAllRequests() {
        return adoptionRequestRepository.findAll();
    }
}
