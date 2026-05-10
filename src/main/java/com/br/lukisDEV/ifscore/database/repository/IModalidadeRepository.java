package com.br.lukisDEV.ifscore.database.repository;

import com.br.lukisDEV.ifscore.database.model.ModalidadeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IModalidadeRepository extends JpaRepository<ModalidadeEntity, UUID> {
}
