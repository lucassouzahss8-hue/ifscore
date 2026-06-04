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
        @Builder.Default
        private Integer bolas2 = 0;
        @PositiveOrZero
        @Builder.Default
        private Integer bolas3 = 0;
        @PositiveOrZero
        @Builder.Default
        private Integer rebotes = 0;
        @PositiveOrZero
        @Builder.Default
        private Integer assistencias = 0;
        @PositiveOrZero
        @Builder.Default
        private Integer lancesLivres = 0;
        @Builder.Default
        @PositiveOrZero
        private Integer faltas = 0;
        @PositiveOrZero
        @Builder.Default
        private Integer roubos = 0;
        @PositiveOrZero
        @Builder.Default
        private Integer tocos = 0;
        @PositiveOrZero
        @Builder.Default
        private Integer totalPontosNoLance = 0;
    }

