package com.br.lukisDEV.ifscore.dto;

import com.br.lukisDEV.ifscore.database.model.AlunoEntity;

import java.util.List;
import java.util.UUID;

public record AlunoResponseDto(
        UUID id,
        String nome,
        String campus,
        Integer numeroRegata,
        List<UUID> modalidadesIds
) {
    public static AlunoResponseDto from(AlunoEntity aluno) {
        List<UUID> modalidadesIds = aluno.getModalidades() == null
                ? List.of()
                : aluno.getModalidades().stream().map(modalidade -> modalidade.getId()).toList();

        return new AlunoResponseDto(
                aluno.getId(),
                aluno.getNome(),
                aluno.getCampus().getNome(),
                aluno.getNumeroRegata(),
                modalidadesIds
        );
    }
}
