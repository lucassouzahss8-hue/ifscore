package com.br.lukisDEV.ifscore.controller;

import com.br.lukisDEV.ifscore.dto.LoginRequestDto;
import com.br.lukisDEV.ifscore.dto.RegisterRequestDto;
import com.br.lukisDEV.ifscore.dto.TokenResponseDto;
import com.br.lukisDEV.ifscore.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private com.br.lukisDEV.ifscore.config.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private com.br.lukisDEV.ifscore.config.TokenProvider tokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_ShouldReturnOk() throws Exception {
        RegisterRequestDto dto = new RegisterRequestDto("Test", "test@gmail.com", "password");

        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(authenticationService, times(1)).register(any(RegisterRequestDto.class));
    }

    @Test
    void login_ShouldReturnToken() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("test@gmail.com", "password");
        TokenResponseDto response = new TokenResponseDto("token", 3600L);

        when(authenticationService.login(any(LoginRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"))
                .andExpect(jsonPath("$.expiresIn").value(3600));

        verify(authenticationService, times(1)).login(any(LoginRequestDto.class));
    }
}
