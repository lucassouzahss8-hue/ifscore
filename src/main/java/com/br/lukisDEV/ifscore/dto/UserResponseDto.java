package com.br.lukisDEV.ifscore.dto;

import com.br.lukisDEV.ifscore.database.model.UserEntity;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String nome,
        String email
        ) {
    public static UserResponseDto from(UserEntity user) {
        return new UserResponseDto(
                user.getId(),
                user.getNome(),
                user.getEmail()
        );
    }
}

