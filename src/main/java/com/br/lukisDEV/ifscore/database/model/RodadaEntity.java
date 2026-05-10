package com.br.lukisDEV.ifscore.database.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "rodada")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RodadaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotBlank
    private String nome;

    @ManyToOne
    private EventoEntity evento;

    @OneToMany(mappedBy = "rodadaEntity", cascade = CascadeType.ALL)
    private List<PartidaEntity> partidas;

}
