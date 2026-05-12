package com.br.lukisDEV.ifscore.database.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "professor")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProfessorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "campus_id", nullable = false)
    private CampusEntity campus;

    @Column (nullable = false)
    private String email;
}
