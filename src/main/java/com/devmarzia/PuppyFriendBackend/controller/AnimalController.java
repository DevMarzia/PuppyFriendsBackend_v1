package com.devmarzia.PuppyFriendBackend.controller;

import com.devmarzia.PuppyFriendBackend.entity.Animal;
import com.devmarzia.PuppyFriendBackend.payload.AnimalDto;
import com.devmarzia.PuppyFriendBackend.service.AnimalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/animals")
public class AnimalController {

    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('VOLUNTEER')")
    public ResponseEntity<Animal> createAnimal(@Valid @RequestBody AnimalDto animalDto) {
        Animal newAnimal = animalService.createAnimal(animalDto);
        return new ResponseEntity<>(newAnimal, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Animal>> getAllAnimals(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String size) {

        List<Animal> animals = animalService.searchAnimals(status, size);
        return ResponseEntity.ok(animals);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Animal> getAnimalById(@PathVariable Long id) {
        Animal animal = animalService.getAnimalById(id);
        return ResponseEntity.ok(animal);
    }

    @PostMapping("/{id}/upload")
    @PreAuthorize("hasRole('ADMIN') or hasRole('VOLUNTEER')") 
    public ResponseEntity<Animal> uploadAnimalImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        Animal updatedAnimal = animalService.uploadImage(id, file);
        return ResponseEntity.ok(updatedAnimal);
    }
}
