package com.br.lukisDEV.ifscore.dto;

import com.br.lukisDEV.ifscore.database.model.ProfessorEntity;

import java.util.UUID;

public record ProfessorResponseDto(
        UUID id,
        String nome,
        String campus,
        String email
) {
    public static ProfessorResponseDto from(ProfessorEntity professor) {
        return new ProfessorResponseDto(
                professor.getId(),
                professor.getNome(),
                professor.getCampus().getNome(),
                professor.getEmail()
        );
    }
}
