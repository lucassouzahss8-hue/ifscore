package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.database.model.AlunoEntity;
import com.br.lukisDEV.ifscore.database.model.EstatisticaEntity;
import com.br.lukisDEV.ifscore.database.repository.IAlunoRepository;
import com.br.lukisDEV.ifscore.database.repository.IEstatisticaRepository;
import com.br.lukisDEV.ifscore.dto.AlunoPerfilDto;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlunoServiceTest {

    @Mock
    private IAlunoRepository alunoRepository;

    @Mock
    private IEstatisticaRepository estatisticaRepository;

    @InjectMocks
    private AlunoService alunoService;

    private UUID alunoId;
    private AlunoEntity alunoEntity;

    @BeforeEach
    void setUp() {
        alunoId = UUID.randomUUID();
        alunoEntity = AlunoEntity.builder()
                .id(alunoId)
                .nome("João Silva")
                .build();
    }

    @Test
    void getAlunoPerfil_WhenAlunoExists_ShouldReturnPerfilWithPontuacao() {
        EstatisticaEntity est1 = EstatisticaEntity.builder()
                .bolas2(2) // 4 pts
                .bolas3(1) // 3 pts
                .lancesLivres(1) // 1 pt
                .build();
        
        EstatisticaEntity est2 = EstatisticaEntity.builder()
                .bolas2(1) // 2 pts
                .bolas3(0) // 0 pts
                .lancesLivres(2) // 2 pts
                .build();

        when(alunoRepository.findById(alunoId)).thenReturn(Optional.of(alunoEntity));
        when(estatisticaRepository.findByAlunoId(alunoId)).thenReturn(List.of(est1, est2));

        AlunoPerfilDto perfil = alunoService.getAlunoPerfil(alunoId);

        assertNotNull(perfil);
        assertEquals("João Silva", perfil.getNome());
        assertEquals(12, perfil.getPontuacao()); // 4+3+1 + 2+0+2 = 12
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
