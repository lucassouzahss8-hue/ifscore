package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.database.model.CampusEntity;
import com.br.lukisDEV.ifscore.database.model.ProfessorEntity;
import com.br.lukisDEV.ifscore.database.repository.IProfessorRepository;
import com.br.lukisDEV.ifscore.dto.ProfessorDto;
import com.br.lukisDEV.ifscore.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfessorService {
    private final IProfessorRepository professorRepository;

    public List<ProfessorEntity> findAll(){
        return professorRepository.findAll();
    }
    public ProfessorEntity save(ProfessorDto professorDto){

        return professorRepository.save(ProfessorEntity.builder()
                        .nome(professorDto.getNome())
                        .email(professorDto.getEmail())
                .build());
    }

    @Transactional
    public ProfessorEntity updateProfessor(UUID id, ProfessorDto professorDto){

        ProfessorEntity professor = professorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Professor nao encontrado"));

                professor.setNome(professorDto.getNome());
                professor.setEmail(professorDto.getEmail());

        return professorRepository.save(professor);
    }
    @Transactional
    public void deleteProfessor(UUID id){
        if (!professorRepository.existsById(id)){
            throw new NotFoundException("Professor nao encontrado");
        }
        professorRepository.deleteById(id);
    }
    public ProfessorEntity findByEmail(String email) {
        return professorRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Professor com o email " + email + " não encontrado"));}
}
