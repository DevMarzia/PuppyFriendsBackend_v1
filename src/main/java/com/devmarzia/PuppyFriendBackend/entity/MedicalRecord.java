package com.devmarzia.PuppyFriendBackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "medical_records")
@Data
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String notes; // Note generali sulla salute
    private boolean sterilized;

    @OneToOne
    @JoinColumn(name = "animal_id", unique = true)
    private Animal animal;

    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("medicalRecord") 
    private List<Vaccination> vaccinations;

    @OneToMany(mappedBy = "medicalRecord", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("medicalRecord")
    private List<VetVisit> vetVisits;


}
