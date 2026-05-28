package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.config.TokenProvider;
import com.br.lukisDEV.ifscore.database.model.AlunoEntity;
import com.br.lukisDEV.ifscore.database.model.ProfessorEntity;
import com.br.lukisDEV.ifscore.database.model.RolesEntity;
import com.br.lukisDEV.ifscore.database.repository.IAlunoRepository;
import com.br.lukisDEV.ifscore.database.repository.IProfessorRepository;
import com.br.lukisDEV.ifscore.database.repository.IRolesRepository;
import com.br.lukisDEV.ifscore.dto.LoginRequestDto;
import com.br.lukisDEV.ifscore.dto.RegisterRequestDto;
import com.br.lukisDEV.ifscore.dto.TokenResponseDto;
import com.br.lukisDEV.ifscore.enums.RoleTypeEnum;
import com.br.lukisDEV.ifscore.exception.EmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private IProfessorRepository professorRepository;
    @Mock
    private IRolesRepository rolesRepository;
    @Mock
    private IAlunoRepository alunoRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authenticationService, "expirationTime", 3600L);
    }

    @Test
    void register_ProfessorEmail_ShouldRegisterProfessor() throws EmailException {
        RegisterRequestDto dto = new RegisterRequestDto("Professor", "test@ifpr.edu.br", "password");
        RolesEntity role = RolesEntity.builder().nome(RoleTypeEnum.ROLE_PROFESSOR.name()).build();

        when(professorRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(alunoRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(rolesRepository.findByNome(RoleTypeEnum.ROLE_PROFESSOR.name())).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(dto.getSenha())).thenReturn("encodedPassword");

        authenticationService.register(dto);

        verify(professorRepository, times(1)).save(any(ProfessorEntity.class));
        verify(alunoRepository, never()).save(any(AlunoEntity.class));
    }

    @Test
    void register_AlunoEmail_ShouldRegisterAluno() throws EmailException {
        RegisterRequestDto dto = new RegisterRequestDto("Aluno", "test@gmail.com", "password");
        RolesEntity role = RolesEntity.builder().nome(RoleTypeEnum.ROLE_ALUNO.name()).build();

        when(professorRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(alunoRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(rolesRepository.findByNome(RoleTypeEnum.ROLE_ALUNO.name())).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(dto.getSenha())).thenReturn("encodedPassword");

        authenticationService.register(dto);

        verify(alunoRepository, times(1)).save(any(AlunoEntity.class));
        verify(professorRepository, never()).save(any(ProfessorEntity.class));
    }

    @Test
    void register_InvalidEmail_ShouldThrowIllegalArgumentException() {
        RegisterRequestDto dto = new RegisterRequestDto("Invalid", "test@invalid.com", "password");

        assertThrows(IllegalArgumentException.class, () -> authenticationService.register(dto));
    }

    @Test
    void register_EmailAlreadyExists_ShouldThrowEmailException() {
        RegisterRequestDto dto = new RegisterRequestDto("Professor", "test@ifpr.edu.br", "password");
        when(professorRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(EmailException.class, () -> authenticationService.register(dto));
    }

    @Test
    void login_Success_ShouldReturnToken() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("test@ifpr.edu.br", "password");
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(tokenProvider.gerarToken(auth)).thenReturn("mock-token");

        TokenResponseDto response = authenticationService.login(dto);

        assertNotNull(response);
        assertEquals("mock-token", response.token());
        assertEquals(3600L, response.expiresIn());
    }

    @Test
    void login_BadCredentials_ShouldThrowBadCredentialsException() {
        LoginRequestDto dto = new LoginRequestDto("test@ifpr.edu.br", "wrong");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () -> authenticationService.login(dto));
    }
}
