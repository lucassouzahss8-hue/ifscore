package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.database.model.EventoEntity;
import com.br.lukisDEV.ifscore.database.repository.IEventoRepository;
import com.br.lukisDEV.ifscore.dto.EventoDto;
import com.br.lukisDEV.ifscore.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    @Mock
    private IEventoRepository eventoRepository;

    @Mock
    private CampusService campusService;

    @InjectMocks
    private EventoService eventoService;

    private EventoEntity eventoEntity;
    private EventoDto eventoDto;
    private UUID eventoId;

    @BeforeEach
    void setUp() {
        eventoId = UUID.randomUUID();
        eventoEntity = EventoEntity.builder()
                .id(eventoId)
                .nome("Evento Teste")
                .local("Local Teste")
                .data(LocalDate.now())
                .build();

        eventoDto = EventoDto.builder()
                .nome("Evento Teste")
                .local("Local Teste")
                .data(LocalDate.now())
                .build();
    }

    @Test
    void findAll_ShouldReturnListOfEventos() {
        when(eventoRepository.findAll()).thenReturn(List.of(eventoEntity));

        List<EventoEntity> result = eventoService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(eventoRepository, times(1)).findAll();
    }

    @Test
    void findById_WhenExists_ShouldReturnEvento() {
        when(eventoRepository.findById(eventoId)).thenReturn(Optional.of(eventoEntity));

        EventoEntity result = eventoService.findById(eventoId);

        assertNotNull(result);
        assertEquals(eventoId, result.getId());
    }

    @Test
    void findById_WhenNotExists_ShouldThrowNotFoundException() {
        when(eventoRepository.findById(eventoId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> eventoService.findById(eventoId));
    }

    @Test
    void criarEvento_ShouldSaveAndReturnEvento() {
        when(eventoRepository.save(any(EventoEntity.class))).thenReturn(eventoEntity);

        EventoEntity result = eventoService.criarEvento(eventoDto);

        assertNotNull(result);
        assertEquals("Evento Teste", result.getNome());
        verify(eventoRepository, times(1)).save(any(EventoEntity.class));
    }

    @Test
    void updateEvento_WhenExists_ShouldUpdateAndReturnEvento() {
        when(eventoRepository.findById(eventoId)).thenReturn(Optional.of(eventoEntity));
        when(eventoRepository.save(any(EventoEntity.class))).thenReturn(eventoEntity);

        EventoEntity result = eventoService.updateEvento(eventoId, eventoDto);

        assertNotNull(result);
        verify(eventoRepository, times(1)).save(any(EventoEntity.class));
    }

    @Test
    void deleteEvento_WhenExists_ShouldDelete() {
        when(eventoRepository.existsById(eventoId)).thenReturn(true);

        eventoService.deleteEvento(eventoId);

        verify(eventoRepository, times(1)).deleteById(eventoId);
    }

    @Test
    void deleteEvento_WhenNotExists_ShouldThrowNotFoundException() {
        when(eventoRepository.existsById(eventoId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> eventoService.deleteEvento(eventoId));
    }
}
