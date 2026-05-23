
package com.br.lukisDEV.ifscore.database.repository;

import com.br.lukisDEV.ifscore.database.model.CampusEntity;
import com.br.lukisDEV.ifscore.database.model.EstatisticaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IEstatisticaRepository extends JpaRepository<EstatisticaEntity, UUID> {

    Optional<EstatisticaEntity> findByPartida_IdAndCampus_Nome(UUID partidaId, String campusNome);

    Optional<EstatisticaEntity> findByPartida_IdAndCampus_NomeAndAluno_Id(UUID partidaId, String campusNome, UUID alunoId);

    java.util.List<EstatisticaEntity> findByAluno_Id(UUID alunoId);

    List<EstatisticaEntity> findByAlunoId(UUID id);
}