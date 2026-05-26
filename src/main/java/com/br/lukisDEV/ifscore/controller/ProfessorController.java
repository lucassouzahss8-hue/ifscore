package com.br.lukisDEV.ifscore.controller;

import com.br.lukisDEV.ifscore.dto.ProfessorDto;
import com.br.lukisDEV.ifscore.dto.ProfessorResponseDto;
import com.br.lukisDEV.ifscore.service.ProfessorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/professor")
@RequiredArgsConstructor
@Validated
public class ProfessorController {
    private final ProfessorService professorService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProfessorResponseDto> findAll(){
        return professorService.findAll().stream().map(ProfessorResponseDto::from).toList();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProfessorResponseDto updateProfessor(@PathVariable UUID id, @Valid @RequestBody ProfessorDto professorDto){
        return ProfessorResponseDto.from(professorService.updateProfessor(id, professorDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfessor(@PathVariable UUID id){
        professorService.deleteProfessor(id);
    }

}
