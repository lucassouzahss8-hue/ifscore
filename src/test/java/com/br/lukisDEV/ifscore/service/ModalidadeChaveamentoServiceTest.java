package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.database.model.CampusEntity;
import com.br.lukisDEV.ifscore.database.model.ModalidadeEntity;
import com.br.lukisDEV.ifscore.database.model.PartidaEntity;
import com.br.lukisDEV.ifscore.database.repository.IModalidadeRepository;
import com.br.lukisDEV.ifscore.database.repository.IPartidaRepository;
import com.br.lukisDEV.ifscore.enums.TipoRodada;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModalidadeChaveamentoServiceTest {

    @Mock
    private IModalidadeRepository modalidadeRepository;
    @Mock
    private IPartidaRepository partidaRepository;
    @Mock
    private CampusService campusService;
    @Mock
    private ModalidadeClassificacaoService classificacaoService;

    @InjectMocks
    private ModalidadeChaveamentoService chaveamentoService;

    private UUID modalidadeId;
    private ModalidadeEntity modalidadeEntity;

    @BeforeEach
    void setUp() {
        modalidadeId = UUID.randomUUID();
        modalidadeEntity = ModalidadeEntity.builder().id(modalidadeId).nome("Basquete").build();
    }

    @Test
    void gerarPartidasDeGrupo_ShouldReturnPartidas() {
        List<CampusEntity> campusList = List.of(
                CampusEntity.builder().nome("C1").build(),
                CampusEntity.builder().nome("C2").build(),
                CampusEntity.builder().nome("C3").build(),
                CampusEntity.builder().nome("C4").build()
        );

        List<PartidaEntity> result = chaveamentoService.gerarPartidasDeGrupo(campusList, modalidadeEntity);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void gerarSemifinal_WhenValid_ShouldSaveAndReturn() {
        List<PartidaEntity> groupPartidas = List.of(
                PartidaEntity.builder().rodada(TipoRodada.GRUPO).finalizada(true).build()
        );
        when(classificacaoService.classificarPorChave(anyList(), eq("A"))).thenReturn(List.of("C1", "C2"));
        when(classificacaoService.classificarPorChave(anyList(), eq("B"))).thenReturn(List.of("C3", "C4"));
        when(modalidadeRepository.findById(modalidadeId)).thenReturn(Optional.of(modalidadeEntity));
        when(campusService.findByNome(anyString())).thenReturn(CampusEntity.builder().nome("Campus").build());
        when(partidaRepository.saveAll(anyList())).thenAnswer(i -> i.getArguments()[0]);

        List<PartidaEntity> result = chaveamentoService.gerarSemifinal(modalidadeId, groupPartidas);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(partidaRepository, times(1)).saveAll(anyList());
    }

    @Test
    void gerarSemifinal_WhenGamesNotFinished_ShouldThrowIllegalStateException() {
        List<PartidaEntity> groupPartidas = List.of(
                PartidaEntity.builder().rodada(TipoRodada.GRUPO).finalizada(false).build()
        );
        assertThrows(IllegalStateException.class, () -> chaveamentoService.gerarSemifinal(modalidadeId, groupPartidas));
    }

    @Test
    void gerarFinal_WhenValid_ShouldSaveAndReturn() {
        List<PartidaEntity> semifinais = List.of(
                PartidaEntity.builder().rodada(TipoRodada.SEMIFINAL).vencedor("C1").finalizada(true).build(),
                PartidaEntity.builder().rodada(TipoRodada.SEMIFINAL).vencedor("C3").finalizada(true).build()
        );
        when(modalidadeRepository.findById(modalidadeId)).thenReturn(Optional.of(modalidadeEntity));
        when(campusService.findByNome(anyString())).thenReturn(CampusEntity.builder().nome("Campus").build());
        when(partidaRepository.save(any(PartidaEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        PartidaEntity result = chaveamentoService.gerarFinal(modalidadeId, semifinais);

        assertNotNull(result);
        assertEquals(TipoRodada.FINAL, result.getRodada());
        verify(partidaRepository, times(1)).save(any(PartidaEntity.class));
    }
}
