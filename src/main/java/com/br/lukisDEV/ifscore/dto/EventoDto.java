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
    @NotNull(message = "A data de início não deve ser nula")
    private LocalDate dataInicio;
    @NotNull(message = "A data de fim não deve ser nula")
    private LocalDate dataFim;
    private List<ModalidadeDto> modalidades;

}
