package com.br.lukisDEV.ifscore.controller;

import com.br.lukisDEV.ifscore.database.model.CampusEntity;
import com.br.lukisDEV.ifscore.database.model.EventoEntity;
import com.br.lukisDEV.ifscore.database.model.ModalidadeEntity;
import com.br.lukisDEV.ifscore.database.model.PartidaEntity;
import com.br.lukisDEV.ifscore.dto.ModalidadeDto;
import com.br.lukisDEV.ifscore.service.ModalidadeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ModalidadeController.class)
@AutoConfigureMockMvc(addFilters = false)
class ModalidadeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ModalidadeService modalidadeService;

    @MockitoBean
    private com.br.lukisDEV.ifscore.config.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private com.br.lukisDEV.ifscore.config.TokenProvider tokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    private ModalidadeEntity modalidadeEntity;
    private ModalidadeDto modalidadeDto;
    private UUID modalidadeId;

    @BeforeEach
    void setUp() {
        modalidadeId = UUID.randomUUID();
        modalidadeEntity = ModalidadeEntity.builder()
                .id(modalidadeId)
                .nome("Futsal")
                .evento(EventoEntity.builder().id(UUID.randomUUID()).build())
                .campus(new ArrayList<>())
                .build();

        modalidadeDto = ModalidadeDto.builder()
                .nome("Futsal")
                .tipo("COLETIVO")
                .eventoId(UUID.randomUUID())
                .campus(List.of("Campus 1"))
                .build();
    }

    @Test
    void findAll_ShouldReturnOk() throws Exception {
        when(modalidadeService.findAll()).thenReturn(List.of(modalidadeEntity));

        mockMvc.perform(get("/v1/modalidade"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Futsal"));

        verify(modalidadeService, times(1)).findAll();
    }

    @Test
    void criar_ShouldReturnCreated() throws Exception {
        when(modalidadeService.criarModalidade(any(ModalidadeDto.class))).thenReturn(modalidadeEntity);

        mockMvc.perform(post("/v1/modalidade/criar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modalidadeDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Futsal"));

        verify(modalidadeService, times(1)).criarModalidade(any(ModalidadeDto.class));
    }

    @Test
    void listarPartidas_ShouldReturnList() throws Exception {
        PartidaEntity partida = PartidaEntity.builder()
                .id(UUID.randomUUID())
                .campus1(CampusEntity.builder().nome("C1").build())
                .campus2(CampusEntity.builder().nome("C2").build())
                .placarCampus1(0)
                .placarCampus2(0)
                .build();
        when(modalidadeService.listarPartidas(modalidadeId)).thenReturn(List.of(partida));

        mockMvc.perform(get("/v1/modalidade/{id}/partidas", modalidadeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].campus1").value("C1"));

        verify(modalidadeService, times(1)).listarPartidas(modalidadeId);
    }

    @Test
    void deleteModalidade_ShouldReturnNoContent() throws Exception {
        doNothing().when(modalidadeService).delete(modalidadeId);

        mockMvc.perform(delete("/v1/modalidade/{id}", modalidadeId))
                .andExpect(status().isNoContent());

        verify(modalidadeService, times(1)).delete(modalidadeId);
    }
}
