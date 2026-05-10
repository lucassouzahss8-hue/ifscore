package com.br.lukisDEV.ifscore.controller;
import com.br.lukisDEV.ifscore.database.model.EventoEntity;
import com.br.lukisDEV.ifscore.dto.EventoDto;
import com.br.lukisDEV.ifscore.service.EventoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

    @RestController
    @RequestMapping("/v1/evento")
    @RequiredArgsConstructor
    public class EventoController {

        private final EventoService eventoService;

        @GetMapping
        @ResponseStatus(HttpStatus.OK)
        public List<EventoEntity> listar() {
            return eventoService.findAll();
        }

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public EventoEntity criar(@RequestBody EventoDto dto) {
            return eventoService.criarEvento(dto);
        }

        @PutMapping("/{id}")
        @ResponseStatus(HttpStatus.CREATED)
        public EventoEntity atualizar(@PathVariable UUID id,
                                      @RequestBody EventoDto dto) {
            return eventoService.updateEvento(id, dto);
        }

        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void deletar(@PathVariable UUID id) {
            eventoService.deleteEvento(id);
        }
    }

