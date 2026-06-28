package com.devmarzia.PuppyFriendBackend.service;

import com.devmarzia.PuppyFriendBackend.entity.Animal;
import com.devmarzia.PuppyFriendBackend.entity.Size;
import com.devmarzia.PuppyFriendBackend.exception.ResourceNotFoundException;
import com.devmarzia.PuppyFriendBackend.payload.AnimalDto;
import com.devmarzia.PuppyFriendBackend.repository.AnimalRepository;
import com.devmarzia.PuppyFriendBackend.repository.SizeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Abilita Mockito
public class AnimalServiceTest {

    @Mock
    private AnimalRepository animalRepository; // Finto DB Animali

    @Mock
    private SizeRepository sizeRepository; // Finto DB Taglie

    @InjectMocks
    private AnimalService animalService; // Il Service vero con dentro i finti DB

    @Test
    void testCreateAnimal_Success() {

        AnimalDto dto = new AnimalDto();
        dto.setName("Fido");
        dto.setSizeLabel("Media");
        dto.setAge(5);
        dto.setStatus("DISPONIBILE");
        dto.setBreed("Meticcio");

        Size mockSize = new Size();
        mockSize.setLabel("Media");

        Animal mockSavedAnimal = new Animal();
        mockSavedAnimal.setId(1L);
        mockSavedAnimal.setName("Fido");
        mockSavedAnimal.setSize(mockSize);

        when(sizeRepository.findByLabel("Media")).thenReturn(Optional.of(mockSize));
        when(animalRepository.save(any(Animal.class))).thenReturn(mockSavedAnimal);

        Animal result = animalService.createAnimal(dto);

        assertNotNull(result); // Il risultato non deve essere null
        assertEquals("Fido", result.getName()); 
        assertEquals("Media", result.getSize().getLabel()); 

        verify(animalRepository, times(1)).save(any(Animal.class));
    }


    // Gestione errore
    @Test
    void testCreateAnimal_SizeNotFound() {

        AnimalDto dto = new AnimalDto();
        dto.setSizeLabel("Gigante"); // Una taglia che non esiste

        when(sizeRepository.findByLabel("Gigante")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            animalService.createAnimal(dto);
        });

        verify(animalRepository, never()).save(any());
    }

    // ricerca per ID
    @Test
    void testGetAnimalById_Success() {

        Animal mockAnimal = new Animal();
        mockAnimal.setId(1L);
        mockAnimal.setName("Rex");

        when(animalRepository.findById(1L)).thenReturn(Optional.of(mockAnimal));

        Animal result = animalService.getAnimalById(1L);

        assertNotNull(result);
        assertEquals("Rex", result.getName());
    }
}
