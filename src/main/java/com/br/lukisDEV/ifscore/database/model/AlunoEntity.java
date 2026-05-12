package com.br.lukisDEV.ifscore.database.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "alunos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AlunoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "campus_id", nullable = false)
    private CampusEntity campus;

    private Integer numeroRegata;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "aluno_modalidade",
            joinColumns = @JoinColumn(name = "aluno_id"),
            inverseJoinColumns = @JoinColumn(name = "modalidade_id")
    )
    private Set<ModalidadeEntity> modalidades = new HashSet<>();
}
