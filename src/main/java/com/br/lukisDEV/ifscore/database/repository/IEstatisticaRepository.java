package com.br.lukisDEV.ifscore.database.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IEstatisticaRepository extends JpaRepository <EstatisticaEntity, UUID>