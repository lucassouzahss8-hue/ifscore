package com.br.lukisDEV.ifscore.dto;

import com.br.lukisDEV.ifscore.database.model.AlunoEntity;

import java.util.List;
import java.util.UUID;

public record AlunoResponseDto(
        UUID id,
        String nome,
        Integer numero,
        CampusResponseDto campus
        ) {
    public static AlunoResponseDto from(AlunoEntity aluno) {
        return new AlunoResponseDto(
                aluno.getId(),
                aluno.getNome(),
                aluno.getNumero(),
                aluno.getCampus() != null ? CampusResponseDto.from(aluno.getCampus()) : null
        );
    }
}

