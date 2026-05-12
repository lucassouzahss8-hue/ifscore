package com.br.lukisDEV.ifscore.controller;

import com.br.lukisDEV.ifscore.dto.EstatisticaDto;
import com.br.lukisDEV.ifscore.dto.EstatisticaResponseDto;
import com.br.lukisDEV.ifscore.dto.ModalidadeDto;
import com.br.lukisDEV.ifscore.dto.ModalidadeResponseDto;
import com.br.lukisDEV.ifscore.dto.PartidaResponseDto;
import com.br.lukisDEV.ifscore.dto.PlacarDto;
import com.br.lukisDEV.ifscore.service.ModalidadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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

    @PutMapping("/partida/{id}/somar")
    @ResponseStatus(HttpStatus.OK)
    public PartidaResponseDto somarPontos(
            @PathVariable UUID id,
            @Valid @RequestBody PlacarDto dto
    ) {

        return PartidaResponseDto.from(modalidadeService.somarPontos(id, dto));
    }
    @PutMapping("/partida/{id}/finalizar")
    @ResponseStatus(HttpStatus.OK)
    public PartidaResponseDto finalizarPartida(@PathVariable UUID id) {

        return PartidaResponseDto.from(modalidadeService.finalizarPartida(id));
    }

    @GetMapping("/{id}/classificacao-simples")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, List<String>> classificacaoSimples(@PathVariable UUID id) {
        return modalidadeService.classificacao(id);
    }

    @GetMapping("/{id}/classificacao")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, List<Map<String, Object>>> classificacaoCompleta(@PathVariable UUID id) {
        return modalidadeService.classificacaoCompleta(id);
    }

    @PostMapping("/{id}/semifinal")
    @ResponseStatus(HttpStatus.CREATED)
    public List<PartidaResponseDto> semifinal(@PathVariable UUID id) {
        return modalidadeService.gerarSemifinal(id).stream().map(PartidaResponseDto::from).toList();
    }

    @PostMapping("/{id}/final")
    @ResponseStatus(HttpStatus.CREATED)
    public PartidaResponseDto finalJogo(@PathVariable UUID id) {
        return PartidaResponseDto.from(modalidadeService.gerarFinal(id));
    }

    @PutMapping("/partida/{id}/estatisticas")
    @ResponseStatus(HttpStatus.OK)
    public EstatisticaResponseDto estatisticas(
            @PathVariable UUID id,
            @Valid @RequestBody EstatisticaDto dto
    ) {

        return EstatisticaResponseDto.from(modalidadeService.atualizarEstatisticas(id, dto));
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteModalidade(@PathVariable UUID id){
        modalidadeService.delete(id);
    }
}
