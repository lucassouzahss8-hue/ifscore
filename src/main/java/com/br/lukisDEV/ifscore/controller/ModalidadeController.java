package com.br.lukisDEV.ifscore.controller;

import com.br.lukisDEV.ifscore.dto.ModalidadeDto;
import com.br.lukisDEV.ifscore.dto.ModalidadeResponseDto;
import com.br.lukisDEV.ifscore.dto.PartidaResponseDto;
import com.br.lukisDEV.ifscore.service.ModalidadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/modalidade")
@RequiredArgsConstructor
public class ModalidadeController {

    private final ModalidadeService modalidadeService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ModalidadeResponseDto>findAll(){
        return modalidadeService.findAll().stream().map(ModalidadeResponseDto::from).toList();
    }

    @PostMapping("/criar")
    @ResponseStatus(HttpStatus.CREATED)
    public ModalidadeResponseDto criar(@Valid @RequestBody ModalidadeDto dto) {
        return ModalidadeResponseDto.from(modalidadeService.criarModalidade(dto));
    }

    @GetMapping("/{id}/partidas")
    public List<PartidaResponseDto> listarPartidas(@PathVariable UUID id){
        return modalidadeService.listarPartidas(id).stream().map(PartidaResponseDto::from).toList();
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteModalidade(@PathVariable UUID id){
        modalidadeService.delete(id);
    }
}
