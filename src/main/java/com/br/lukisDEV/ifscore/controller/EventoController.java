package com.br.lukisDEV.ifscore.controller;
import com.br.lukisDEV.ifscore.dto.EventoDto;
import com.br.lukisDEV.ifscore.dto.EventoResponseDto;
import com.br.lukisDEV.ifscore.service.EventoService;
import jakarta.validation.Valid;
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
        public List<EventoResponseDto> listar() {
            return eventoService.findAll().stream().map(EventoResponseDto::from).toList();
        }

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public EventoResponseDto criar(@Valid @RequestBody EventoDto dto) {
            return EventoResponseDto.from(eventoService.criarEvento(dto));
        }

        @PutMapping("/{id}")
        @ResponseStatus(HttpStatus.OK)
        public EventoResponseDto atualizar(@PathVariable UUID id,
                                           @Valid @RequestBody EventoDto dto) {
            return EventoResponseDto.from(eventoService.updateEvento(id, dto));
        }

        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void deletar(@PathVariable UUID id) {
            eventoService.deleteEvento(id);
        }
    }

