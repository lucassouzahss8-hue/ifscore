package com.br.lukisDEV.ifscore.database.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "chaveamento")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ChaveamentoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
}
