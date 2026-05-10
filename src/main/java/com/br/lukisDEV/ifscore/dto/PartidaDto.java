package com.br.lukisDEV.ifscore.dto;

import com.br.lukisDEV.ifscore.enums.TipoRodada;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PartidaDto{
    @NotBlank
    private String campus1;
    @NotBlank
    private String campus2;
    @NotNull
    private TipoRodada rodada;
    @NotNull
    private Integer placarCampus1;
    @NotNull
    private Integer placarCampus2;
    @NotBlank
    private String vencedor;
    @NotBlank
    private String chave;
}