package com.br.lukisDEV.ifscore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PlacarDto {
        @NotBlank
        private String time;
        @NotNull
        @Positive
        private Integer pontos;
    }

