package com.br.lukisDEV.ifscore.dto;

import com.br.lukisDEV.ifscore.database.model.CampusEntity;

import java.util.UUID;

public record CampusResponseDto(
        UUID id,
        String nome,
        String regiao
) {
    public static CampusResponseDto from(CampusEntity campus) {
        return new CampusResponseDto(campus.getId(), campus.getNome(), campus.getRegiao());
    }
}
