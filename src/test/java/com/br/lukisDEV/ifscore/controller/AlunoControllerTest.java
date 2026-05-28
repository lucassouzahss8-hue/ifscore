package com.br.lukisDEV.ifscore.controller;

import com.br.lukisDEV.ifscore.database.model.AlunoEntity;
import com.br.lukisDEV.ifscore.dto.AlunoPerfilDto;
import com.br.lukisDEV.ifscore.service.AlunoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlunoController.class)
@AutoConfigureMockMvc(addFilters = false)
class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AlunoService alunoService;

    @MockitoBean
    private com.br.lukisDEV.ifscore.config.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private com.br.lukisDEV.ifscore.config.TokenProvider tokenProvider;

    @Test
    void getPerfil_ShouldReturnOk() throws Exception {
        UUID id = UUID.randomUUID();
        AlunoPerfilDto perfil = AlunoPerfilDto.builder().nome("João").pontuacao(10).build();
        when(alunoService.getAlunoPerfil(id)).thenReturn(perfil);

        mockMvc.perform(get("/v1/aluno/{id}/perfil", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João"))
                .andExpect(jsonPath("$.pontuacao").value(10));
    }

    @Test
    void findAll_ShouldReturnOk() throws Exception {
        AlunoEntity aluno = AlunoEntity.builder().id(UUID.randomUUID()).nome("João").build();
        when(alunoService.findAll()).thenReturn(List.of(aluno));

        mockMvc.perform(get("/v1/aluno"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João"));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(alunoService).deleteAluno(id);

        mockMvc.perform(delete("/v1/aluno/{id}", id))
                .andExpect(status().isNoContent());

        verify(alunoService).deleteAluno(id);
    }
}
