package com.br.lukisDEV.ifscore.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class EstatisticaDto {
    @NotBlank
    private String campus;

    private java.util.UUID alunoId;

    private Integer cestas = 0;

    private Integer bolas2 = 0;

    private Integer bolas3 = 0;

    private Integer rebotes = 0;

    private Integer assistencias = 0;

    private Integer lancesLivres = 0;

    private Integer faltas = 0;
}
