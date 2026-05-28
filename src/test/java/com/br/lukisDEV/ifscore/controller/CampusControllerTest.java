package com.br.lukisDEV.ifscore.controller;

import com.br.lukisDEV.ifscore.database.model.CampusEntity;
import com.br.lukisDEV.ifscore.dto.CampusDto;
import com.br.lukisDEV.ifscore.service.CampusService;
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

@WebMvcTest(CampusController.class)
@AutoConfigureMockMvc(addFilters = false)
class CampusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CampusService campusService;

    @MockitoBean
    private com.br.lukisDEV.ifscore.config.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private com.br.lukisDEV.ifscore.config.TokenProvider tokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    private CampusEntity campusEntity;
    private CampusDto campusDto;
    private UUID campusId;

    @BeforeEach
    void setUp() {
        campusId = UUID.randomUUID();
        campusEntity = CampusEntity.builder()
                .id(campusId)
                .nome("Campus Teste")
                .regiao("Regiao Teste")
                .build();

        campusDto = CampusDto.builder()
                .nome("Campus Teste")
                .regiao("Regiao Teste")
                .build();
    }

    @Test
    void findAll_ShouldReturnOkAndList() throws Exception {
        when(campusService.findAll()).thenReturn(List.of(campusEntity));

        mockMvc.perform(get("/v1/campus"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Campus Teste"))
                .andExpect(jsonPath("$[0].regiao").value("Regiao Teste"));

        verify(campusService, times(1)).findAll();
    }

    @Test
    void saveCampus_ShouldReturnCreated() throws Exception {
        mockMvc.perform(post("/v1/campus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(campusDto)))
                .andExpect(status().isCreated());

        verify(campusService, times(1)).save(any(CampusDto.class));
    }

    @Test
    void updateCampus_ShouldReturnOkAndCampus() throws Exception {
        when(campusService.updateCampus(eq(campusId), any(CampusDto.class))).thenReturn(campusEntity);

        mockMvc.perform(put("/v1/campus/{id}", campusId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(campusDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Campus Teste"));

        verify(campusService, times(1)).updateCampus(eq(campusId), any(CampusDto.class));
    }

    @Test
    void deleteCampus_ShouldReturnNoContent() throws Exception {
        doNothing().when(campusService).deleteCampus(campusId);

        mockMvc.perform(delete("/v1/campus/{id}", campusId))
                .andExpect(status().isNoContent());

        verify(campusService, times(1)).deleteCampus(campusId);
    }
}
