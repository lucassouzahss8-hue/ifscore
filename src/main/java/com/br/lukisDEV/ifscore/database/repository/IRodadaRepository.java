package com.br.lukisDEV.ifscore.database.repository;

import com.br.lukisDEV.ifscore.database.model.RodadaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IRodadaRepository extends JpaRepository<RodadaEntity, UUID> {
}
