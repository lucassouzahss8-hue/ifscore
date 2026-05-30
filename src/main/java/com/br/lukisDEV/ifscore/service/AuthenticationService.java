package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.config.TokenProvider;
import com.br.lukisDEV.ifscore.database.model.AlunoEntity;
import com.br.lukisDEV.ifscore.database.model.ProfessorEntity;
import com.br.lukisDEV.ifscore.database.model.RolesEntity;
import com.br.lukisDEV.ifscore.database.model.UserEntity;
import com.br.lukisDEV.ifscore.database.repository.IAlunoRepository;
import com.br.lukisDEV.ifscore.database.repository.IProfessorRepository;
import com.br.lukisDEV.ifscore.database.repository.IRolesRepository;
import com.br.lukisDEV.ifscore.database.repository.IUserRepository;
import com.br.lukisDEV.ifscore.dto.LoginRequestDto;
import com.br.lukisDEV.ifscore.dto.RegisterRequestDto;
import com.br.lukisDEV.ifscore.dto.TokenResponseDto;
import com.br.lukisDEV.ifscore.enums.RoleTypeEnum;
import com.br.lukisDEV.ifscore.exception.EmailException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    private final IAlunoRepository alunoRepository;
    private final IUserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    @Value("${jwt.expiration}")
    private long expirationTime;


    @Transactional
    public void register(RegisterRequestDto dto) throws EmailException {
        String email = dto.getEmail();
        RoleTypeEnum roleType;
        if (email.endsWith("@ifpr.edu.br")) {
            roleType = RoleTypeEnum.ROLE_PROFESSOR;
        } else if (email.endsWith("@gmail.com")) {
            roleType = RoleTypeEnum.ROLE_USER;
        } else {
            throw new  IllegalArgumentException("Email não aceito");
        }


        if (professorRepository.existsByEmail(email) || userRepository.existsByEmail(email)) {
            throw new EmailException("Já existe um usuario cadastrado com este email");
        }

        RolesEntity role = rolesRepository.findByNome(roleType.name())
                .orElseGet(() -> rolesRepository.save(RolesEntity.builder()
                        .nome(roleType.name())
                        .build()));
        if (roleType == RoleTypeEnum.ROLE_PROFESSOR) {
            professorRepository.save(ProfessorEntity.builder()
                    .nome(dto.getNome())
                    .email(dto.getEmail())
                    .roles(Set.of(role))
                    .senha(passwordEncoder.encode(dto.getSenha()))
                    .build());
        } else {
            if (userRepository.existsByEmail(email)) {
                throw new EmailException("Já existe aluno cadastrado com este email");
            }
            userRepository.save(UserEntity.builder()
                    .nome(dto.getNome())
                    .email(dto.getEmail())
                    .roles(Set.of(role))
                    .senha(passwordEncoder.encode(dto.getSenha()))
                    .build());

        }
    }


        public TokenResponseDto login (LoginRequestDto dto) throws Exception {
            try {
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha()));
                String token = tokenProvider.gerarToken(authentication);

                return new TokenResponseDto(token, expirationTime);

            } catch (BadCredentialsException ex) {
                throw new BadCredentialsException(ex.getMessage());
            } catch (Exception e) {
                throw new Exception("Internal Error", e);
            }
        }
    }
