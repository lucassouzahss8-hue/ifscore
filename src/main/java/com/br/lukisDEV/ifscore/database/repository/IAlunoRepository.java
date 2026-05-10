package com.br.lukisDEV.ifscore.database.repository;

import com.br.lukisDEV.ifscore.database.model.AlunoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IAlunoRepository extends JpaRepository<AlunoEntity, UUID> {
}
