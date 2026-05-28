package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.database.model.CampusEntity;
import com.br.lukisDEV.ifscore.database.model.EventoEntity;
import com.br.lukisDEV.ifscore.database.model.ModalidadeEntity;
import com.br.lukisDEV.ifscore.database.model.PartidaEntity;
import com.br.lukisDEV.ifscore.database.repository.IEventoRepository;
import com.br.lukisDEV.ifscore.database.repository.IModalidadeRepository;
import com.br.lukisDEV.ifscore.database.repository.IPartidaRepository;
import com.br.lukisDEV.ifscore.dto.ModalidadeDto;
import com.br.lukisDEV.ifscore.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModalidadeServiceTest {

    @Mock
    private IEventoRepository eventoRepository;
    @Mock
    private IModalidadeRepository modalidadeRepository;
    @Mock
    private IPartidaRepository partidaRepository;
    @Mock
    private CampusService campusService;
    @Mock
    private ModalidadeClassificacaoService classificacaoService;
    @Mock
    private ModalidadeChaveamentoService chaveamentoService;

    @InjectMocks
    private ModalidadeService modalidadeService;

    private UUID modalidadeId;
    private ModalidadeEntity modalidadeEntity;
    private ModalidadeDto modalidadeDto;
    private EventoEntity eventoEntity;

    @BeforeEach
    void setUp() {
        modalidadeId = UUID.randomUUID();
        eventoEntity = EventoEntity.builder().id(UUID.randomUUID()).nome("Evento").build();
        modalidadeEntity = ModalidadeEntity.builder()
                .id(modalidadeId)
                .nome("Basquete")
                .evento(eventoEntity)
                .campus(new ArrayList<>())
                .partidas(new ArrayList<>())
                .build();

        modalidadeDto = ModalidadeDto.builder()
                .nome("Basquete")
                .tipo("COLETIVO")
                .eventoId(eventoEntity.getId())
                .campus(List.of("Campus 1"))
                .build();
    }

    @Test
    void findAll_ShouldReturnList() {
        when(modalidadeRepository.findAll()).thenReturn(List.of(modalidadeEntity));
        List<ModalidadeEntity> result = modalidadeService.findAll();
        assertFalse(result.isEmpty());
        verify(modalidadeRepository, times(1)).findAll();
    }

    @Test
    void criarModalidade_ShouldSaveAndGeneratePartidas() {
        when(eventoRepository.findById(any())).thenReturn(Optional.of(eventoEntity));
        when(campusService.findByNome(anyString())).thenReturn(CampusEntity.builder().nome("Campus 1").build());
        when(modalidadeRepository.save(any(ModalidadeEntity.class))).thenReturn(modalidadeEntity);
        when(chaveamentoService.gerarPartidasDeGrupo(anyList(), any())).thenReturn(List.of(new PartidaEntity()));

        ModalidadeEntity result = modalidadeService.criarModalidade(modalidadeDto);

        assertNotNull(result);
        verify(modalidadeRepository, times(1)).save(any(ModalidadeEntity.class));
        verify(partidaRepository, times(1)).saveAll(anyList());
    }

    @Test
    void listarPartidas_ShouldReturnPartidas() {
        when(partidaRepository.findByModalidadeId(modalidadeId)).thenReturn(List.of(new PartidaEntity()));
        List<PartidaEntity> result = modalidadeService.listarPartidas(modalidadeId);
        assertFalse(result.isEmpty());
    }

    @Test
    void delete_WhenExists_ShouldDelete() {
        when(modalidadeRepository.existsById(modalidadeId)).thenReturn(true);
        modalidadeService.delete(modalidadeId);
        verify(modalidadeRepository, times(1)).deleteById(modalidadeId);
    }

    @Test
    void delete_WhenNotExists_ShouldThrowNotFoundException() {
        when(modalidadeRepository.existsById(modalidadeId)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> modalidadeService.delete(modalidadeId));
    }
}
