package com.devmarzia.PuppyFriendBackend.service;

import com.devmarzia.PuppyFriendBackend.entity.Animal;
import com.devmarzia.PuppyFriendBackend.entity.Size;
import com.devmarzia.PuppyFriendBackend.exception.ResourceNotFoundException;
import com.devmarzia.PuppyFriendBackend.payload.AnimalDto;
import com.devmarzia.PuppyFriendBackend.repository.AnimalRepository;
import com.devmarzia.PuppyFriendBackend.repository.SizeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final SizeRepository sizeRepository;

    public AnimalService(AnimalRepository animalRepository, SizeRepository sizeRepository) {
        this.animalRepository = animalRepository;
        this.sizeRepository = sizeRepository;
    }

    // INSERIMENTO ANIMALE
    public Animal createAnimal(AnimalDto dto) {
        // Cerchiamo la taglia nel DB
        Size size = sizeRepository.findByLabel(dto.getSizeLabel())
                .orElseThrow(() -> new ResourceNotFoundException("Taglia", "label", dto.getSizeLabel()));;

        // Creiamo l'entity
        Animal animal = new Animal();
        animal.setName(dto.getName());
        animal.setBreed(dto.getBreed());
        animal.setAge(dto.getAge());
        // Se non viene passata una data, usa la data odierna
        animal.setEntryDate(dto.getEntryDate() != null ? dto.getEntryDate() : LocalDate.now());
        animal.setStatus(dto.getStatus().toUpperCase());
        animal.setSize(size);
        animal.setImageUrl(dto.getImageUrl());

        return animalRepository.save(animal);
    }

    // RICERCA CON FILTRI E ORDINAMENTO
    public List<Animal> searchAnimals(String status, String sizeLabel) {
        // Se ho sia STATO che TAGLIA -> filtro per entrambi
        if (status != null && sizeLabel != null) {
            return animalRepository.findBySize_LabelAndStatus(sizeLabel, status.toUpperCase());
        }
        // Se ho solo lo STATO -> filtro per stato
        else if (status != null) {
            return animalRepository.findByStatus(status.toUpperCase());
        }
        // Solo filtro TAGLIA
        else if (sizeLabel != null) {
            return animalRepository.findBySize_Label(sizeLabel);
        }
        // Se non ho filtri restituisce tutto ordinato per data di ingresso (i più recenti prima)
        else {
            return animalRepository.findAll(Sort.by(Sort.Direction.DESC, "entryDate"));
        }

    }

    // RICERCA PER ID
    public Animal getAnimalById(Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Animale", "id", id));
    }

    public Animal uploadImage(Long id, MultipartFile file) {
        // Trova l'animale
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Animale", "id", id));

        // URL finto (Simulazione)
        String fileName = file.getOriginalFilename();
        String fakeUrl = "http://localhost:8080/uploads/" + fileName;

        // Salva l'URL nel database
        animal.setImageUrl(fakeUrl);
        return animalRepository.save(animal);
    }
}
