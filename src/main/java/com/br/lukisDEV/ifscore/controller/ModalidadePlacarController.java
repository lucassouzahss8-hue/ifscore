package com.br.lukisDEV.ifscore.controller;

import com.br.lukisDEV.ifscore.dto.EstatisticaResponseDto;
import com.br.lukisDEV.ifscore.dto.PartidaResponseDto;
import com.br.lukisDEV.ifscore.dto.PlacarDto;
import com.br.lukisDEV.ifscore.service.ModalidadePlacarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/placar")
@RequiredArgsConstructor
public class ModalidadePlacarController {

    private final ModalidadePlacarService modalidadePlacarService;

    @PutMapping("/partida/{id}/estatistica")
    @ResponseStatus(HttpStatus.CREATED)
    public EstatisticaResponseDto atualizarPlacarEstatisticas(
            @PathVariable UUID id,
            @RequestBody @Valid PlacarDto dto
    ) {
        return modalidadePlacarService.atualizarPlacarEstatisticas(id, dto);
    }

    @PutMapping("/partida/{id}/finalizar")
    @ResponseStatus(HttpStatus.CREATED)
    public PartidaResponseDto finalizarPartida(@PathVariable UUID id) {
        return PartidaResponseDto.from(modalidadePlacarService.finalizarPartida(id));
    }
}
