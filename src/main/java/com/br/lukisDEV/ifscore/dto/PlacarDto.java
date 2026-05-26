package com.br.lukisDEV.ifscore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PlacarDto {
        @NotBlank
        private String campus;

        private UUID alunoId;
        @PositiveOrZero
        private Integer bolas2 = 0;
        @PositiveOrZero
        private Integer bolas3 = 0;
        @PositiveOrZero
        private Integer rebotes = 0;
        @PositiveOrZero
        private Integer assistencias = 0;
        @PositiveOrZero
        private Integer lancesLivres = 0;
        private Integer faltas = 0;
    }

