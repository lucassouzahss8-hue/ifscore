package com.br.lukisDEV.ifscore.database.model;

import com.br.lukisDEV.ifscore.enums.TipoRodada;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "partida")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartidaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus1_id")
    private CampusEntity campus1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus2_id")
    private CampusEntity campus2;

    @Builder.Default
    private Integer placarCampus1 = 0;

    @Builder.Default
    private Integer placarCampus2 = 0;

    private String vencedor;

    private String chave;

    @Enumerated(EnumType.STRING)
    private TipoRodada rodada;

    @Builder.Default
    private Boolean finalizada = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modalidade_id")
    @JsonBackReference
    private ModalidadeEntity modalidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rodada_id")
    private RodadaEntity rodadaEntity;
}