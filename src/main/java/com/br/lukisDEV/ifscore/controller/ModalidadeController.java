package com.br.lukisDEV.ifscore.controller;

import com.br.lukisDEV.ifscore.database.model.EstatisticaEntity;
import com.br.lukisDEV.ifscore.database.model.ModalidadeEntity;
import com.br.lukisDEV.ifscore.database.model.PartidaEntity;
import com.br.lukisDEV.ifscore.dto.EstatisticaDto;
import com.br.lukisDEV.ifscore.dto.ModalidadeDto;
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
    public List<ModalidadeEntity>findAll(){
        return modalidadeService.findAll();
    }


    @PostMapping("/criar")
    @ResponseStatus(HttpStatus.CREATED)
    public ModalidadeEntity criar(@RequestBody @Valid ModalidadeDto dto) {
        return modalidadeService.criarModalidade(dto);
    }

    @GetMapping("/{id}/partidas")
    public List<PartidaEntity> listarPartidas(@PathVariable UUID id){
        return modalidadeService.listarPartidas(id);
    }

    @PutMapping("/partida/{id}/somar")
    @ResponseStatus(HttpStatus.OK)
    public PartidaEntity somarPontos(
            @PathVariable UUID id,
            @RequestBody PlacarDto dto
    ) {

        return modalidadeService.somarPontos(id, dto);
    }
    @PutMapping("/partida/{id}/finalizar")
    @ResponseStatus(HttpStatus.OK)
    public PartidaEntity finalizarPartida(@PathVariable UUID id) {

        return modalidadeService.finalizarPartida(id);
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
    public List<PartidaEntity> semifinal(@PathVariable UUID id) {
        return modalidadeService.gerarSemifinal(id);
    }

    @PostMapping("/{id}/final")
    @ResponseStatus(HttpStatus.CREATED)
    public PartidaEntity finalJogo(@PathVariable UUID id) {
        return modalidadeService.gerarFinal(id);
    }

    @PutMapping("/partida/{id}/estatisticas")
    @ResponseStatus(HttpStatus.OK)
    public EstatisticaEntity estatisticas(
            @PathVariable UUID id,
            @RequestBody EstatisticaDto dto
    ) {

        return modalidadeService.atualizarEstatisticas(id, dto);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteModalidade(@PathVariable UUID id){
        modalidadeService.delete(id);
    }
}
