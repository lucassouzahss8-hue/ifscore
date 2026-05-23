package com.br.lukisDEV.ifscore.dto;

import com.br.lukisDEV.ifscore.database.model.EventoEntity;

import java.time.LocalDate;
import java.util.UUID;

public record EventoResponseDto(
        UUID id,
        String nome,
        String local,
        LocalDate data
) {
    public static EventoResponseDto from(EventoEntity evento) {
        return new EventoResponseDto(evento.getId(), evento.getNome(), evento.getLocal(), evento.getData());
    }
}
