package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.database.model.CampusEntity;
import com.br.lukisDEV.ifscore.database.repository.ICampusRepository;
import com.br.lukisDEV.ifscore.dto.CampusDto;
import com.br.lukisDEV.ifscore.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CampusServiceTest {

    @Mock
    private ICampusRepository campusRepository;

    @InjectMocks
    private CampusService campusService;

    private CampusEntity campusEntity;
    private CampusDto campusDto;
    private UUID campusId;

    @BeforeEach
    void setUp() {
        campusId = UUID.randomUUID();
        campusEntity = CampusEntity.builder()
                .id(campusId)
                .nome("Campus Teste")
                .regiao("Regiao Teste")
                .build();
        
        campusDto = CampusDto.builder()
                .nome("Campus Teste")
                .regiao("Regiao Teste")
                .build();
    }

    @Test
    void findAll_ShouldReturnListOfCampus() {
        when(campusRepository.findAll()).thenReturn(List.of(campusEntity));

        List<CampusEntity> result = campusService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Campus Teste", result.get(0).getNome());
        verify(campusRepository, times(1)).findAll();
    }

    @Test
    void save_ShouldSaveCampus() {
        campusService.save(campusDto);
        verify(campusRepository, times(1)).save(any(CampusEntity.class));
    }

    @Test
    void updateCampus_WhenCampusExists_ShouldUpdateAndReturnCampus() {
        when(campusRepository.findById(campusId)).thenReturn(Optional.of(campusEntity));
        when(campusRepository.save(any(CampusEntity.class))).thenReturn(campusEntity);

        CampusEntity updatedCampus = campusService.updateCampus(campusId, campusDto);

        assertNotNull(updatedCampus);
        assertEquals("Campus Teste", updatedCampus.getNome());
        verify(campusRepository, times(1)).findById(campusId);
        verify(campusRepository, times(1)).save(any(CampusEntity.class));
    }

    @Test
    void updateCampus_WhenCampusDoesNotExist_ShouldThrowNotFoundException() {
        when(campusRepository.findById(campusId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> campusService.updateCampus(campusId, campusDto));
        verify(campusRepository, times(1)).findById(campusId);
        verify(campusRepository, never()).save(any());
    }

    @Test
    void deleteCampus_WhenCampusExists_ShouldDelete() {
        when(campusRepository.existsById(campusId)).thenReturn(true);

        campusService.deleteCampus(campusId);

        verify(campusRepository, times(1)).existsById(campusId);
        verify(campusRepository, times(1)).deleteById(campusId);
    }

    @Test
    void deleteCampus_WhenCampusDoesNotExist_ShouldThrowNotFoundException() {
        when(campusRepository.existsById(campusId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> campusService.deleteCampus(campusId));
        verify(campusRepository, times(1)).existsById(campusId);
        verify(campusRepository, never()).deleteById(any());
    }

    @Test
    void findByNome_WhenExists_ShouldReturnCampus() {
        when(campusRepository.findByNome("Campus Teste")).thenReturn(Optional.of(campusEntity));

        CampusEntity result = campusService.findByNome("Campus Teste");

        assertNotNull(result);
        assertEquals("Campus Teste", result.getNome());
    }

    @Test
    void findByNome_WhenNotExists_ShouldThrowNotFoundException() {
        when(campusRepository.findByNome("Inexistente")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> campusService.findByNome("Inexistente"));
    }
}
