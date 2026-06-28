package com.devmarzia.PuppyFriendBackend.controller;

import com.devmarzia.PuppyFriendBackend.entity.MedicalRecord;
import com.devmarzia.PuppyFriendBackend.entity.Vaccination;
import com.devmarzia.PuppyFriendBackend.entity.VetVisit;
import com.devmarzia.PuppyFriendBackend.service.VetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vet")
public class VetController {

    @Autowired
    private VetService vetService;

    // Crea Cartella Clinica
    @PostMapping("/animals/{animalId}/record")
    public ResponseEntity<MedicalRecord> createRecord(@PathVariable Long animalId, @RequestBody MedicalRecord record) {
        return ResponseEntity.ok(vetService.createMedicalRecord(animalId, record));
    }

    // Aggiunge Vaccino
    @PostMapping("/records/{recordId}/vaccines")
    public ResponseEntity<Vaccination> addVaccine(@PathVariable Long recordId, @RequestBody Vaccination vaccine) {
        return ResponseEntity.ok(vetService.addVaccine(recordId, vaccine));
    }

    // Visualizza tutto
    @GetMapping("/animals/{animalId}")
    public ResponseEntity<MedicalRecord> getFullHistory(@PathVariable Long animalId) {
        return ResponseEntity.ok(vetService.getMedicalRecordByAnimal(animalId));
    }

    // Aggiunge Visita
    @PostMapping("/records/{recordId}/visits")
    public ResponseEntity<VetVisit> addVisit(@PathVariable Long recordId, @RequestBody VetVisit visit) {
        return ResponseEntity.ok(vetService.addVisit(recordId, visit));
    }
}
