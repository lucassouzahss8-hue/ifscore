package com.br.lukisDEV.ifscore.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlunoPerfilDto {
    private String nome;
    private String campus;
    private Integer numeroRegata;
    private Integer pontuacao;
    private Integer cestas;
    private Integer bolas2;
    private Integer bolas3;
    private Integer lancesLivres;
    private Integer rebotes;
    private Integer assistencias;
    private Integer faltas;
}