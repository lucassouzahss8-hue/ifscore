package com.br.lukisDEV.ifscore.controller;

import com.br.lukisDEV.ifscore.database.model.CampusEntity;
import com.br.lukisDEV.ifscore.database.model.PartidaEntity;
import com.br.lukisDEV.ifscore.enums.TipoRodada;
import com.br.lukisDEV.ifscore.service.ModalidadeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ModalidadeChaveamentoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ModalidadeChaveamentoControllerTest {

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

    @Test
    void gerarSemifinal_ShouldReturnCreated() throws Exception {
        UUID modalidadeId = UUID.randomUUID();
        PartidaEntity semifinal = PartidaEntity.builder()
                .id(UUID.randomUUID())
                .campus1(CampusEntity.builder().nome("C1").build())
                .campus2(CampusEntity.builder().nome("C2").build())
                .rodada(TipoRodada.SEMIFINAL)
                .build();

        when(modalidadeService.gerarSemifinal(modalidadeId)).thenReturn(List.of(semifinal));

        mockMvc.perform(post("/v1/chaveamento/{id}/semifinal", modalidadeId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].rodada").value("SEMIFINAL"));

        verify(modalidadeService, times(1)).gerarSemifinal(modalidadeId);
    }

    @Test
    void gerarFinal_ShouldReturnCreated() throws Exception {
        UUID modalidadeId = UUID.randomUUID();
        PartidaEntity finalJogo = PartidaEntity.builder()
                .id(UUID.randomUUID())
                .campus1(CampusEntity.builder().nome("C1").build())
                .campus2(CampusEntity.builder().nome("C2").build())
                .rodada(TipoRodada.FINAL)
                .build();

        when(modalidadeService.gerarFinal(modalidadeId)).thenReturn(finalJogo);

        mockMvc.perform(post("/v1/chaveamento/{id}/final", modalidadeId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rodada").value("FINAL"));

        verify(modalidadeService, times(1)).gerarFinal(modalidadeId);
    }
}
