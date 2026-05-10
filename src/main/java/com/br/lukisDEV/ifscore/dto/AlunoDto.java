package com.br.lukisDEV.ifscore.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AlunoDto {
    @NotBlank
    private String nome;
    @NotBlank
    private String campus;

    private Integer numeroRegata;

    private List<UUID> modalidadesIds;
}