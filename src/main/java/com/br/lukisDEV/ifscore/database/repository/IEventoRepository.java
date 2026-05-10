package com.br.lukisDEV.ifscore.database.repository;

import com.br.lukisDEV.ifscore.database.model.EventoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IEventoRepository extends JpaRepository<EventoEntity, UUID> {
}
