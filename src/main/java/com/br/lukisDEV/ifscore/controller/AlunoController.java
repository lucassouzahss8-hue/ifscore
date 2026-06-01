package com.br.lukisDEV.ifscore.controller;

import com.br.lukisDEV.ifscore.dto.AlunoDto;
import com.br.lukisDEV.ifscore.dto.AlunoPerfilDto;
import com.br.lukisDEV.ifscore.dto.AlunoResponseDto;
import com.br.lukisDEV.ifscore.service.AlunoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
        return alunoService.findAll().stream().map(AlunoResponseDto::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AlunoResponseDto> createAluno(@Valid @RequestBody AlunoDto dto) {
        AlunoResponseDto alunoResponseDto = alunoService.salvarAluno(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(alunoResponseDto.id())
                .toUri();

        return  ResponseEntity.created(location).body(alunoResponseDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        alunoService.deleteAluno(id);
    }
}
