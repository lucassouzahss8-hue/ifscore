package com.br.lukisDEV.ifscore.controller;

import com.br.lukisDEV.ifscore.database.model.EventoEntity;
import com.br.lukisDEV.ifscore.dto.EventoDto;
import com.br.lukisDEV.ifscore.service.EventoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventoController.class)
@AutoConfigureMockMvc(addFilters = false)
class EventoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventoService eventoService;

    @MockitoBean
    private com.br.lukisDEV.ifscore.config.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private com.br.lukisDEV.ifscore.config.TokenProvider tokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

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
    void listar_ShouldReturnOkAndList() throws Exception {
        when(eventoService.findAll()).thenReturn(List.of(eventoEntity));

        mockMvc.perform(get("/v1/evento"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Evento Teste"))
                .andExpect(jsonPath("$[0].local").value("Local Teste"));

        verify(eventoService, times(1)).findAll();
    }

    @Test
    void criar_ShouldReturnCreated() throws Exception {
        when(eventoService.criarEvento(any(EventoDto.class))).thenReturn(eventoEntity);

        mockMvc.perform(post("/v1/evento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventoDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Evento Teste"));

        verify(eventoService, times(1)).criarEvento(any(EventoDto.class));
    }

    @Test
    void atualizar_ShouldReturnOk() throws Exception {
        when(eventoService.updateEvento(eq(eventoId), any(EventoDto.class))).thenReturn(eventoEntity);

        mockMvc.perform(put("/v1/evento/{id}", eventoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Evento Teste"));

        verify(eventoService, times(1)).updateEvento(eq(eventoId), any(EventoDto.class));
    }

    @Test
    void deletar_ShouldReturnNoContent() throws Exception {
        doNothing().when(eventoService).deleteEvento(eventoId);

        mockMvc.perform(delete("/v1/evento/{id}", eventoId))
                .andExpect(status().isNoContent());

        verify(eventoService, times(1)).deleteEvento(eventoId);
    }
}
