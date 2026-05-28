package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.database.model.AlunoEntity;
import com.br.lukisDEV.ifscore.database.model.CampusEntity;
import com.br.lukisDEV.ifscore.database.model.EstatisticaEntity;
import com.br.lukisDEV.ifscore.database.model.PartidaEntity;
import com.br.lukisDEV.ifscore.database.repository.IAlunoRepository;
import com.br.lukisDEV.ifscore.database.repository.IEstatisticaRepository;
import com.br.lukisDEV.ifscore.database.repository.IPartidaRepository;
import com.br.lukisDEV.ifscore.dto.EstatisticaResponseDto;
import com.br.lukisDEV.ifscore.dto.PlacarDto;
import com.br.lukisDEV.ifscore.enums.TipoRodada;
import com.br.lukisDEV.ifscore.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModalidadePlacarServiceTest {

    @Mock
    private IAlunoRepository alunoRepository;
    @Mock
    private IPartidaRepository partidaRepository;
    @Mock
    private IEstatisticaRepository estatisticaRepository;
    @Mock
    private CampusService campusService;

    @InjectMocks
    private ModalidadePlacarService modalidadePlacarService;

    private UUID partidaId;
    private PartidaEntity partida;
    private CampusEntity campus1;
    private CampusEntity campus2;

    @BeforeEach
    void setUp() {
        partidaId = UUID.randomUUID();
        campus1 = CampusEntity.builder().nome("Campus 1").build();
        campus2 = CampusEntity.builder().nome("Campus 2").build();
        partida = PartidaEntity.builder()
                .id(partidaId)
                .campus1(campus1)
                .campus2(campus2)
                .placarCampus1(0)
                .placarCampus2(0)
                .finalizada(false)
                .rodada(TipoRodada.GRUPO)
                .build();
    }

    @Test
    void atualizarPlacarEstatisticas_Success() {
        PlacarDto dto = PlacarDto.builder()
                .campus("Campus 1")
                .bolas2(1) // 2 pts
                .bolas3(1) // 3 pts
                .lancesLivres(1) // 1 pt
                .build();

        when(partidaRepository.findById(partidaId)).thenReturn(Optional.of(partida));
        when(campusService.findByNome("Campus 1")).thenReturn(campus1);
        when(estatisticaRepository.findByPartida_IdAndCampus_NomeAndAlunoIsNull(partidaId, "Campus 1"))
                .thenReturn(Optional.empty());

        EstatisticaResponseDto result = modalidadePlacarService.atualizarPlacarEstatisticas(partidaId, dto);

        assertEquals(6, result.getTotalPontosNoLance());
        assertEquals(6, partida.getPlacarCampus1());
        verify(partidaRepository).save(partida);
        verify(estatisticaRepository).save(any(EstatisticaEntity.class));
    }

    @Test
    void finalizarPartida_Success() {
        partida.setPlacarCampus1(10);
        partida.setPlacarCampus2(5);
        when(partidaRepository.findById(partidaId)).thenReturn(Optional.of(partida));
        when(partidaRepository.save(any(PartidaEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        PartidaEntity result = modalidadePlacarService.finalizarPartida(partidaId);

        assertTrue(result.getFinalizada());
        assertEquals("Campus 1", result.getVencedor());
    }

    @Test
    void finalizarPartida_Error_AlreadyFinalized() {
        partida.setFinalizada(true);
        when(partidaRepository.findById(partidaId)).thenReturn(Optional.of(partida));

        assertThrows(RuntimeException.class, () -> modalidadePlacarService.finalizarPartida(partidaId));
    }
}
