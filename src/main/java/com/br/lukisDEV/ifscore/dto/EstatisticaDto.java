package com.br.lukisDEV.ifscore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
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

    @PositiveOrZero
    @Builder.Default
    private Integer cestas = 0;

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

    @PositiveOrZero
    @Builder.Default
    private Integer faltas = 0;
}
