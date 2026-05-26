package com.br.lukisDEV.ifscore.database.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "evento")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EventoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String local;
    @Column(nullable = false)
    private LocalDate data;

    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private java.util.Set<ModalidadeEntity> modalidades = new java.util.HashSet<>();
}