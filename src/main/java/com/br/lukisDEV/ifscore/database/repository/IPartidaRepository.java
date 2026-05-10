package com.br.lukisDEV.ifscore.database.repository;

import com.br.lukisDEV.ifscore.database.model.PartidaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IPartidaRepository extends JpaRepository<PartidaEntity, UUID> {

    List<PartidaEntity> findByModalidadeId(UUID modalidadeId);
    }