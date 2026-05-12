package com.br.lukisDEV.ifscore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ModalidadeDto {
    @NotBlank
    private String nome;
    @NotBlank
    private String tipo;
    @NotEmpty
    private List<String> campus;
    @NotNull
    private UUID eventoId;

}
