package com.br.lukisDEV.ifscore.controller;

import com.br.lukisDEV.ifscore.database.model.ModalidadeEntity;
import com.br.lukisDEV.ifscore.database.model.ProfessorEntity;
import com.br.lukisDEV.ifscore.dto.ModalidadeDto;
import com.br.lukisDEV.ifscore.dto.ProfessorDto;
import com.br.lukisDEV.ifscore.service.ModalidadeService;
import com.br.lukisDEV.ifscore.service.ProfessorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/professor")
@RequiredArgsConstructor
@Validated
public class ProfessorController {
    private final ProfessorService professorService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProfessorEntity> findAll(){
        return professorService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveProfessor
        (@Valid @RequestBody ProfessorDto professorDto){
            professorService.save(professorDto);
    }

}
