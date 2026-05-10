package com.br.lukisDEV.ifscore.database.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "modalidade")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ModalidadeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String nome;
    @ManyToOne
    @JoinColumn(name = "evento_id")
    @JsonBackReference
    private EventoEntity evento;

    @ElementCollection
    private List<String> campus;

    @OneToMany(mappedBy = "modalidade", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PartidaEntity> partidas = new ArrayList<>();
}
