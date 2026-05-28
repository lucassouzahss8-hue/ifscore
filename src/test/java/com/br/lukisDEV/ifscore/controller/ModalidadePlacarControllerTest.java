package com.br.lukisDEV.ifscore.controller;

import com.br.lukisDEV.ifscore.database.model.CampusEntity;
import com.br.lukisDEV.ifscore.database.model.PartidaEntity;
import com.br.lukisDEV.ifscore.dto.EstatisticaResponseDto;
import com.br.lukisDEV.ifscore.dto.PlacarDto;
import com.br.lukisDEV.ifscore.service.ModalidadePlacarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ModalidadePlacarController.class)
@AutoConfigureMockMvc(addFilters = false)
class ModalidadePlacarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ModalidadePlacarService modalidadePlacarService;

    @MockitoBean
    private com.br.lukisDEV.ifscore.config.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private com.br.lukisDEV.ifscore.config.TokenProvider tokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void atualizarPlacarEstatisticas_ShouldReturnCreated() throws Exception {
        UUID id = UUID.randomUUID();
        PlacarDto dto = PlacarDto.builder().campus("Campus 1").build();
        EstatisticaResponseDto response = EstatisticaResponseDto.builder().campus("Campus 1").totalPontosNoLance(5).build();

        when(modalidadePlacarService.atualizarPlacarEstatisticas(eq(id), any(PlacarDto.class))).thenReturn(response);

        mockMvc.perform(put("/v1/placar/partida/{id}/estatistica", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.campus").value("Campus 1"))
                .andExpect(jsonPath("$.totalPontosNoLance").value(5));
    }

    @Test
    void finalizarPartida_ShouldReturnCreated() throws Exception {
        UUID id = UUID.randomUUID();
        PartidaEntity partida = PartidaEntity.builder()
                .id(id)
                .campus1(CampusEntity.builder().nome("C1").build())
                .campus2(CampusEntity.builder().nome("C2").build())
                .placarCampus1(10)
                .placarCampus2(5)
                .vencedor("C1")
                .finalizada(true)
                .build();

        when(modalidadePlacarService.finalizarPartida(id)).thenReturn(partida);

        mockMvc.perform(put("/v1/placar/partida/{id}/finalizar", id))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.vencedor").value("C1"))
                .andExpect(jsonPath("$.finalizada").value(true));
    }
}
