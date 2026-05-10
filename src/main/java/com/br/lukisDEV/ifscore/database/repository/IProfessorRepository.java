package com.br.lukisDEV.ifscore.database.repository;

import com.br.lukisDEV.ifscore.database.model.ProfessorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IProfessorRepository extends JpaRepository<ProfessorEntity, UUID> {
}
