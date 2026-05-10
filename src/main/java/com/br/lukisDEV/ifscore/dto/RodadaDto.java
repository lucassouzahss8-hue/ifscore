package com.br.lukisDEV.ifscore.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RodadaDto {
    @NotBlank
    private String nome;

    private List<PartidaDto> partidas;
}