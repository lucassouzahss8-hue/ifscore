package com.br.lukisDEV.ifscore.controller;

import com.br.lukisDEV.ifscore.dto.PartidaResponseDto;
import com.br.lukisDEV.ifscore.service.ModalidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/chaveamento")
@RequiredArgsConstructor
public class ModalidadeChaveamentoController {

    private final ModalidadeService modalidadeService;

    @PostMapping("/{id}/semifinal")
    @ResponseStatus(HttpStatus.CREATED)
    public List<PartidaResponseDto> gerarSemifinal(@PathVariable UUID id) {
        return modalidadeService.gerarSemifinal(id).stream()
                .map(PartidaResponseDto::from)
                .toList();
    }

    @PostMapping("/{id}/final")
    @ResponseStatus(HttpStatus.CREATED)
    public PartidaResponseDto gerarFinal(@PathVariable UUID id) {
        return PartidaResponseDto.from(modalidadeService.gerarFinal(id));
    }
}
