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
}