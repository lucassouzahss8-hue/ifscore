package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.database.model.AlunoEntity;
import com.br.lukisDEV.ifscore.database.model.CampusEntity;
import com.br.lukisDEV.ifscore.database.model.EstatisticaEntity;
import com.br.lukisDEV.ifscore.database.repository.IAlunoRepository;
import com.br.lukisDEV.ifscore.database.repository.IEstatisticaRepository;
import com.br.lukisDEV.ifscore.dto.AlunoDto;
import com.br.lukisDEV.ifscore.dto.AlunoPerfilDto;
import com.br.lukisDEV.ifscore.dto.AlunoResponseDto;
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
class AlunoServiceTest {

    @Mock
    private IAlunoRepository alunoRepository;

    @Mock
    private IEstatisticaRepository estatisticaRepository;

    @Mock
    private CampusService campusService;

    @InjectMocks
    private AlunoService alunoService;

    private UUID alunoId;
    private AlunoEntity alunoEntity;
    private CampusEntity campusEntity;

    @BeforeEach
    void setUp() {
        alunoId = UUID.randomUUID();
        campusEntity = CampusEntity.builder()
                .id(UUID.randomUUID())
                .nome("Campus Teste")
                .regiao("Norte")
                .build();
        alunoEntity = AlunoEntity.builder()
                .id(alunoId)
                .nome("João Silva")
                .numero(10)
                .campus(campusEntity)
                .build();
    }

    @Test
    void salvarAluno_ShouldReturnResponse() {
        AlunoDto dto = AlunoDto.builder()
                .nome("João Silva")
                .numero(10)
                .campusId(campusEntity.getId())
                .build();

        when(campusService.findById(campusEntity.getId())).thenReturn(campusEntity);
        when(alunoRepository.save(any(AlunoEntity.class))).thenReturn(alunoEntity);

        AlunoResponseDto response = alunoService.salvarAluno(dto);

        assertNotNull(response);
        assertEquals("João Silva", response.nome());
        assertEquals("Campus Teste", response.campus().nome());
        verify(campusService).findById(campusEntity.getId());
        verify(alunoRepository).save(any(AlunoEntity.class));
    }

    @Test
    void getAlunoPerfil_WhenAlunoExists_ShouldReturnPerfilWithAllStats() {
        com.br.lukisDEV.ifscore.database.model.CampusEntity campus = com.br.lukisDEV.ifscore.database.model.CampusEntity.builder()
                .nome("Campus Teste")
                .build();

        EstatisticaEntity est1 = EstatisticaEntity.builder()
                .campus(campus)
                .bolas2(2) // 4 pts
                .bolas3(1) // 3 pts
                .lancesLivres(1) // 1 pt
                .cestas(3)
                .rebotes(2)
                .assistencias(1)
                .faltas(1)
                .build();
        
        EstatisticaEntity est2 = EstatisticaEntity.builder()
                .campus(campus)
                .bolas2(1) // 2 pts
                .bolas3(0) // 0 pts
                .lancesLivres(2) // 2 pts
                .cestas(1)
                .rebotes(3)
                .assistencias(2)
                .faltas(0)
                .build();

        alunoEntity.setNumero(10);
        when(alunoRepository.findById(alunoId)).thenReturn(Optional.of(alunoEntity));
        when(estatisticaRepository.findByAlunoId(alunoId)).thenReturn(List.of(est1, est2));

        AlunoPerfilDto perfil = alunoService.getAlunoPerfil(alunoId);

        assertNotNull(perfil);
        assertEquals("João Silva", perfil.getNome());
        assertEquals("Campus Teste", perfil.getCampus());
        assertEquals(10, perfil.getNumeroRegata());
        assertEquals(12, perfil.getPontuacao()); // 4+3+1 + 2+0+2 = 12
        assertEquals(4, perfil.getCestas());
        assertEquals(3, perfil.getBolas2());
        assertEquals(1, perfil.getBolas3());
        assertEquals(3, perfil.getLancesLivres());
        assertEquals(5, perfil.getRebotes());
        assertEquals(3, perfil.getAssistencias());
        assertEquals(1, perfil.getFaltas());
        
        verify(alunoRepository).findById(alunoId);
        verify(estatisticaRepository).findByAlunoId(alunoId);
    }

    @Test
    void getAlunoPerfil_WhenAlunoDoesNotExist_ShouldThrowNotFoundException() {
        when(alunoRepository.findById(alunoId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> alunoService.getAlunoPerfil(alunoId));
    }

    @Test
    void findAll_ShouldReturnList() {
        when(alunoRepository.findAll()).thenReturn(List.of(alunoEntity));

        List<AlunoEntity> result = alunoService.findAll();

        assertEquals(1, result.size());
        assertEquals("João Silva", result.get(0).getNome());
    }

    @Test
    void deleteAluno_WhenExists_ShouldDelete() {
        when(alunoRepository.existsById(alunoId)).thenReturn(true);

        alunoService.deleteAluno(alunoId);

        verify(alunoRepository).deleteById(alunoId);
    }

    @Test
    void deleteAluno_WhenNotExists_ShouldThrowNotFoundException() {
        when(alunoRepository.existsById(alunoId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> alunoService.deleteAluno(alunoId));
    }
}
