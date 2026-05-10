package com.br.lukisDEV.ifscore.database.repository;

import com.br.lukisDEV.ifscore.database.model.CampusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ICampusRepository extends JpaRepository<CampusEntity, UUID> {
    boolean existsByNome(String nome);
}
