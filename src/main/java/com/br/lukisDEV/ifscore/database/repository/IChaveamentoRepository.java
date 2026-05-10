package com.br.lukisDEV.ifscore.database.repository;

import com.br.lukisDEV.ifscore.database.model.ChaveamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IChaveamentoRepository extends JpaRepository<ChaveamentoEntity, UUID> {
}
