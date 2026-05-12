package com.br.lukisDEV.ifscore.controller;

import com.br.lukisDEV.ifscore.database.model.AlunoEntity;
import com.br.lukisDEV.ifscore.dto.AlunoDto;
import com.br.lukisDEV.ifscore.dto.AlunoPerfilDto;
import com.br.lukisDEV.ifscore.service.AlunoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/aluno")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;

    @GetMapping("/{id}/perfil")
    @ResponseStatus(HttpStatus.OK)
    public AlunoPerfilDto getPerfil(@PathVariable UUID id) {
        return alunoService.getAlunoPerfil(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AlunoResponseDto> findAll() {
        return alunoService.findAll().stream()
                .map(AlunoResponseDto::fromEntity)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlunoResponseDto create(@RequestBody AlunoDto dto) {
        return AlunoResponseDto.fromEntity(alunoService.save(dto));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AlunoResponseDto update(@PathVariable UUID id, @RequestBody AlunoDto dto) {
        return AlunoResponseDto.fromEntity(alunoService.updateAluno(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        alunoService.deleteAluno(id);
    }
}