package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.database.model.ProfessorEntity;
import com.br.lukisDEV.ifscore.database.repository.IAlunoRepository;
import com.br.lukisDEV.ifscore.database.repository.IProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IProfessorRepository professorRepository;
    private final IAlunoRepository alunoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ProfessorEntity> professor =  professorRepository.findByEmail(username);
        if (professor.isPresent()) {
            return (UserDetails) professor.get();
        }
        return alunoRepository.findByEmail(username)
                .map(alunoEntity -> (UserDetails) alunoEntity)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o email " + username));
    }
}
