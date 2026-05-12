package com.br.lukisDEV.ifscore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class EventoDto {
    @NotBlank
    private String nome;
    @NotBlank
    private String local;
    @NotNull
    private LocalDate data;
    private List<ModalidadeDto> modalidades;

}
