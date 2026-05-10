package com.br.lukisDEV.ifscore.dto;

import jakarta.validation.constraints.NotBlank;
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
        @NotBlank
        private Integer pontos;
    }

