package com.br.lukisDEV.ifscore.dto;

import com.br.lukisDEV.ifscore.database.model.AlunoEntity;

import java.util.List;
import java.util.UUID;

public record AlunoResponseDto(
        UUID id,
        String nome,
        String email
        ) {
    public static AlunoResponseDto from(AlunoEntity aluno) {
        return new AlunoResponseDto(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail()
        );
    }
}

