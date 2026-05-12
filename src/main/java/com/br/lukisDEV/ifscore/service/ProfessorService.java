package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.database.model.AlunoEntity;
import com.br.lukisDEV.ifscore.database.model.CampusEntity;
import com.br.lukisDEV.ifscore.database.model.ProfessorEntity;
import com.br.lukisDEV.ifscore.database.repository.IProfessorRepository;
import com.br.lukisDEV.ifscore.dto.AlunoDto;
import com.br.lukisDEV.ifscore.dto.ProfessorDto;
import com.br.lukisDEV.ifscore.exception.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfessorService {
    private final IProfessorRepository professorRepository;
    private final CampusService campusService;

    public List<ProfessorEntity> findAll(){
        return professorRepository.findAll();
    }
    public void save(ProfessorDto professorDto){
        CampusEntity campus = campusService.findByNome(professorDto.getCampus());

        professorRepository.save(ProfessorEntity.builder()
                        .nome(professorDto.getNome())
                        .campus(campus)
                        .email(professorDto.getEmail())
                .build());
    }

    @Transactional
    public ProfessorEntity updateProfessor(UUID id, ProfessorDto professorDto){

        CampusEntity campus = campusService.findByNome(professorDto.getCampus());

        ProfessorEntity professor = professorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Professor não encontrado"));

                professor.setNome(professorDto.getNome());
                professor.setCampus(campus);
                professor.setEmail(professorDto.getEmail());

        return professorRepository.save(professor);
    }
    @Transactional
    public void deleteProfessor(UUID id){
        if (!professorRepository.existsById(id)){
            throw new EntityNotFoundException("Professor não encontrado");
        }
        professorRepository.deleteById(id);
    }
}
