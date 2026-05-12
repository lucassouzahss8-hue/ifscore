package com.br.lukisDEV.ifscore.database.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "estatisticas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EstatisticaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus_id", nullable = false)
    private CampusEntity campus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id")
    private AlunoEntity aluno;

    @Builder.Default
    private Integer cestas = 0;

    @Builder.Default
    private Integer bolas2 = 0;

    @Builder.Default
    private Integer bolas3 = 0;

    @Builder.Default
    private Integer rebotes = 0;

    @Builder.Default
    private Integer assistencias = 0;

    @Builder.Default
    private Integer lancesLivres = 0;

    @Builder.Default
    private  Integer faltas = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partida_id")
    @JsonBackReference
    private PartidaEntity partida;
}
