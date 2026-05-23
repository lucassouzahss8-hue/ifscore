package com.br.lukisDEV.ifscore.dto;

import com.br.lukisDEV.ifscore.database.model.ModalidadeEntity;

import java.util.List;
import java.util.UUID;

public record ModalidadeResponseDto(
        UUID id,
        String nome,
        UUID eventoId,
        List<String> campus
) {
    public static ModalidadeResponseDto from(ModalidadeEntity modalidade) {
        UUID eventoId = modalidade.getEvento() == null ? null : modalidade.getEvento().getId();
        List<String> campus = modalidade.getCampus() == null
                ? List.of()
                : modalidade.getCampus().stream().map(c -> c.getNome()).toList();

        return new ModalidadeResponseDto(
                modalidade.getId(),
                modalidade.getNome(),
                eventoId,
                campus
        );
    }
}
