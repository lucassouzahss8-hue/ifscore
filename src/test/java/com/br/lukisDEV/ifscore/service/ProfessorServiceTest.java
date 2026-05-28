package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.database.model.ProfessorEntity;
import com.br.lukisDEV.ifscore.database.repository.IProfessorRepository;
import com.br.lukisDEV.ifscore.dto.ProfessorDto;
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
class ProfessorServiceTest {

    @Mock
    private IProfessorRepository professorRepository;

    @InjectMocks
    private ProfessorService professorService;

    private ProfessorEntity professorEntity;
    private ProfessorDto professorDto;
    private UUID professorId;

    @BeforeEach
    void setUp() {
        professorId = UUID.randomUUID();
        professorEntity = ProfessorEntity.builder()
                .id(professorId)
                .nome("Professor Teste")
                .email("professor@test.com")
                .build();

        professorDto = ProfessorDto.builder()
                .nome("Professor Teste")
                .email("professor@test.com")
                .senha("password")
                .build();
    }

    @Test
    void findAll_ShouldReturnListOfProfessors() {
        when(professorRepository.findAll()).thenReturn(List.of(professorEntity));

        List<ProfessorEntity> result = professorService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(professorRepository, times(1)).findAll();
    }

    @Test
    void save_ShouldSaveProfessor() {
        when(professorRepository.save(any(ProfessorEntity.class))).thenReturn(professorEntity);

        ProfessorEntity result = professorService.save(professorDto);

        assertNotNull(result);
        verify(professorRepository, times(1)).save(any(ProfessorEntity.class));
    }

    @Test
    void updateProfessor_WhenExists_ShouldUpdateAndReturnProfessor() {
        when(professorRepository.findById(professorId)).thenReturn(Optional.of(professorEntity));
        when(professorRepository.save(any(ProfessorEntity.class))).thenReturn(professorEntity);

        ProfessorEntity result = professorService.updateProfessor(professorId, professorDto);

        assertNotNull(result);
        verify(professorRepository, times(1)).findById(professorId);
        verify(professorRepository, times(1)).save(any(ProfessorEntity.class));
    }

    @Test
    void updateProfessor_WhenNotExists_ShouldThrowNotFoundException() {
        when(professorRepository.findById(professorId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> professorService.updateProfessor(professorId, professorDto));
    }

    @Test
    void deleteProfessor_WhenExists_ShouldDelete() {
        when(professorRepository.existsById(professorId)).thenReturn(true);

        professorService.deleteProfessor(professorId);

        verify(professorRepository, times(1)).existsById(professorId);
        verify(professorRepository, times(1)).deleteById(professorId);
    }

    @Test
    void deleteProfessor_WhenNotExists_ShouldThrowNotFoundException() {
        when(professorRepository.existsById(professorId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> professorService.deleteProfessor(professorId));
    }

    @Test
    void findByEmail_WhenExists_ShouldReturnProfessor() {
        when(professorRepository.findByEmail("professor@test.com")).thenReturn(Optional.of(professorEntity));

        ProfessorEntity result = professorService.findByEmail("professor@test.com");

        assertNotNull(result);
        assertEquals("professor@test.com", result.getEmail());
    }

    @Test
    void findByEmail_WhenNotExists_ShouldThrowNotFoundException() {
        when(professorRepository.findByEmail("non@test.com")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> professorService.findByEmail("non@test.com"));
    }
}
