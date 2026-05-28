package com.br.lukisDEV.ifscore.controller;

import com.br.lukisDEV.ifscore.service.ModalidadeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ModalidadeClassificacaoController.class)
@AutoConfigureMockMvc(addFilters = false)
class ModalidadeClassificacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ModalidadeService modalidadeService;

    @MockitoBean
    private com.br.lukisDEV.ifscore.config.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private com.br.lukisDEV.ifscore.config.TokenProvider tokenProvider;

    @Test
    void getClassificacao_ShouldReturnOk() throws Exception {
        UUID modalidadeId = UUID.randomUUID();
        when(modalidadeService.classificacao(modalidadeId)).thenReturn(Map.of("chaveA", List.of("C1")));

        mockMvc.perform(get("/v1/classificacao/{id}", modalidadeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chaveA[0]").value("C1"));

        verify(modalidadeService, times(1)).classificacao(modalidadeId);
    }

    @Test
    void getClassificacaoCompleta_ShouldReturnOk() throws Exception {
        UUID modalidadeId = UUID.randomUUID();
        when(modalidadeService.classificacaoCompleta(modalidadeId)).thenReturn(Map.of("chaveA", List.of(Map.of("time", "C1"))));

        mockMvc.perform(get("/v1/classificacao/{id}/completa", modalidadeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chaveA[0].time").value("C1"));

        verify(modalidadeService, times(1)).classificacaoCompleta(modalidadeId);
    }
}
