package com.br.lukisDEV.ifscore.database.repository;

import com.br.lukisDEV.ifscore.database.model.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IRolesRepository extends JpaRepository<RolesEntity, UUID> {
    Optional<RolesEntity> findByNome(String nome);
}
