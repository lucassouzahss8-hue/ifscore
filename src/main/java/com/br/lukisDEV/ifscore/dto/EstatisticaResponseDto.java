package com.br.lukisDEV.ifscore.dto;

import com.br.lukisDEV.ifscore.database.model.EstatisticaEntity;

import java.util.UUID;

public record EstatisticaResponseDto(
        UUID id,
        String campus,
        UUID alunoId,
        Integer cestas,
        Integer bolas2,
        Integer bolas3,
        Integer rebotes,
        Integer assistencias,
        Integer lancesLivres,
        Integer faltas
) {
    public static EstatisticaResponseDto from(EstatisticaEntity estatistica) {
        UUID alunoId = estatistica.getAluno() == null ? null : estatistica.getAluno().getId();

        return new EstatisticaResponseDto(
                estatistica.getId(),
                estatistica.getCampus().getNome(),
                alunoId,
                estatistica.getCestas(),
                estatistica.getBolas2(),
                estatistica.getBolas3(),
                estatistica.getRebotes(),
                estatistica.getAssistencias(),
                estatistica.getLancesLivres(),
                estatistica.getFaltas()
        );
    }
}
