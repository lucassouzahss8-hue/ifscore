package com.br.lukisDEV.ifscore.controller;

import com.br.lukisDEV.ifscore.database.model.ProfessorEntity;
import com.br.lukisDEV.ifscore.dto.ProfessorDto;
import com.br.lukisDEV.ifscore.service.ProfessorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfessorController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProfessorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProfessorService professorService;

    @MockitoBean
    private com.br.lukisDEV.ifscore.config.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private com.br.lukisDEV.ifscore.config.TokenProvider tokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    private ProfessorEntity professorEntity;
    private ProfessorDto professorDto;
    private UUID professorId;

    @BeforeEach
    void setUp() {
        professorId = UUID.randomUUID();
        professorEntity = ProfessorEntity.builder()
                .id(professorId)
                .nome("Professor Teste")
                .email("professor@test.com")
                .build();

        professorDto = ProfessorDto.builder()
                .nome("Professor Teste")
                .email("professor@test.com")
                .senha("password")
                .build();
    }

    @Test
    void findAll_ShouldReturnOkAndList() throws Exception {
        when(professorService.findAll()).thenReturn(List.of(professorEntity));

        mockMvc.perform(get("/v1/professor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Professor Teste"))
                .andExpect(jsonPath("$[0].email").value("professor@test.com"));

        verify(professorService, times(1)).findAll();
    }

    @Test
    void updateProfessor_ShouldReturnOk() throws Exception {
        when(professorService.updateProfessor(eq(professorId), any(ProfessorDto.class))).thenReturn(professorEntity);

        mockMvc.perform(put("/v1/professor/{id}", professorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(professorDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Professor Teste"));

        verify(professorService, times(1)).updateProfessor(eq(professorId), any(ProfessorDto.class));
    }

    @Test
    void deleteProfessor_ShouldReturnNoContent() throws Exception {
        doNothing().when(professorService).deleteProfessor(professorId);

        mockMvc.perform(delete("/v1/professor/{id}", professorId))
                .andExpect(status().isNoContent());

        verify(professorService, times(1)).deleteProfessor(professorId);
    }
}
