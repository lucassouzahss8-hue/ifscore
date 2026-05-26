package com.br.lukisDEV.ifscore.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EstatisticaResponseDto {
    private String campus;
    private String alunoNome;
    private Integer cestas;
    private Integer bolas2;
    private Integer bolas3;
    private Integer rebotes;
    private Integer assistencias;
    private Integer lancesLivres;
    private Integer faltas;
    private Integer totalPontosNoLance;
    }