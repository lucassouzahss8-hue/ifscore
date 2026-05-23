package com.br.lukisDEV.ifscore.dto;

import com.br.lukisDEV.ifscore.database.model.PartidaEntity;
import com.br.lukisDEV.ifscore.enums.TipoRodada;

import java.util.UUID;

public record PartidaResponseDto(
        UUID id,
        String campus1,
        String campus2,
        Integer placarCampus1,
        Integer placarCampus2,
        String vencedor,
        String chave,
        TipoRodada rodada,
        Boolean finalizada
) {
    public static PartidaResponseDto from(PartidaEntity partida) {
        return new PartidaResponseDto(
                partida.getId(),
                partida.getCampus1().getNome(),
                partida.getCampus2().getNome(),
                partida.getPlacarCampus1(),
                partida.getPlacarCampus2(),
                partida.getVencedor(),
                partida.getChave(),
                partida.getRodada(),
                partida.getFinalizada()
        );
    }
}
