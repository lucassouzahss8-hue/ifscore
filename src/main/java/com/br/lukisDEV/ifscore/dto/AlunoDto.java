package com.br.lukisDEV.ifscore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AlunoDto {
    @NotBlank
    private String nome;
    @PositiveOrZero
    private Integer numero;
    @NotNull
    private UUID campusId;
}