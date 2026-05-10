package com.br.lukisDEV.ifscore.database.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "campus")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CampusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String regiao;
}
