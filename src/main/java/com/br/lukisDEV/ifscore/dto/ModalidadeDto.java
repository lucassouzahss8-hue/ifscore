package com.br.lukisDEV.ifscore.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.apache.catalina.LifecycleState;

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
    private List<String> campus;
    private UUID eventoId;

}
