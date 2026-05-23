package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.config.TokenProvider;
import com.br.lukisDEV.ifscore.database.model.CampusEntity;
import com.br.lukisDEV.ifscore.database.model.ProfessorEntity;
import com.br.lukisDEV.ifscore.database.model.RolesEntity;
import com.br.lukisDEV.ifscore.database.repository.IProfessorRepository;
import com.br.lukisDEV.ifscore.database.repository.IRolesRepository;
import com.br.lukisDEV.ifscore.dto.LoginRequestDto;
import com.br.lukisDEV.ifscore.dto.RegisterRequestDto;
import com.br.lukisDEV.ifscore.dto.TokenResponseDto;
import com.br.lukisDEV.ifscore.enums.RoleTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final IProfessorRepository professorRepository;
    private final IRolesRepository rolesRepository;
    private final CampusService campusService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    @Value("${jwt.expiration}")
    private long expirationTime;

    @SneakyThrows
    public void register(RegisterRequestDto dto) {
        ProfessorEntity professor = professorRepository.findByEmail(dto.getEmail())
                .orElse(null);

        if (professor != null) {
            throw new BadRequestException("Professor ja cadastrado com este email");
        }

        CampusEntity campus = campusService.findByNome(dto.getCampus());

        RolesEntity role = rolesRepository.findByNome(RoleTypeEnum.ROLE_PROFESSOR.name())
                .orElseGet(() -> rolesRepository.save(RolesEntity.builder()
                        .nome(RoleTypeEnum.ROLE_PROFESSOR.name())
                        .build()));
        professorRepository.save(ProfessorEntity.builder()
                .nome(dto.getNome())
                .campus(campus)
                .email(dto.getEmail())
                .roles(Set.of(role))
                .senha(passwordEncoder.encode(dto.getSenha()))
                .build());
    }

    public TokenResponseDto login(LoginRequestDto dto) throws Exception {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha()));
            String token = tokenProvider.gerarToken(auth);

        return new TokenResponseDto(token, expirationTime);
        } catch (BadCredentialsException e) {
            throw new BadRequestException("Credenciais Invalidas");
        } catch (Exception e) {
            throw e;
        }
    }
}
