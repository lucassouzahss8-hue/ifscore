
package com.br.lukisDEV.ifscore.database.repository;

import com.br.lukisDEV.ifscore.database.model.EstatisticaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IEstatisticaRepository extends JpaRepository<EstatisticaEntity, UUID> {

    Optional<EstatisticaEntity> findByPartida_IdAndCampus(UUID partidaId, String campus);

    Optional<EstatisticaEntity> findByPartida_IdAndCampusAndAluno_Id(UUID partidaId, String campus, UUID alunoId);

    java.util.List<EstatisticaEntity> findByAluno_Id(UUID alunoId);
    }