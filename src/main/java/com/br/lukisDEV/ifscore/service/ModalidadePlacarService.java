package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.database.model.AlunoEntity;
import com.br.lukisDEV.ifscore.database.model.CampusEntity;
import com.br.lukisDEV.ifscore.database.model.EstatisticaEntity;
import com.br.lukisDEV.ifscore.database.model.PartidaEntity;
import com.br.lukisDEV.ifscore.database.repository.IAlunoRepository;
import com.br.lukisDEV.ifscore.database.repository.IEstatisticaRepository;
import com.br.lukisDEV.ifscore.database.repository.IPartidaRepository;
import com.br.lukisDEV.ifscore.dto.EstatisticaResponseDto;
import com.br.lukisDEV.ifscore.dto.PlacarDto;
import com.br.lukisDEV.ifscore.enums.TipoRodada;
import com.br.lukisDEV.ifscore.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ModalidadePlacarService {
    private final IAlunoRepository alunoRepository;
    private final IPartidaRepository partidaRepository;
    private final IEstatisticaRepository estatisticaRepository;
    private final CampusService campusService;

    @Transactional
    public EstatisticaResponseDto atualizarPlacarEstatisticas(UUID partidaId, PlacarDto dto) {
        PartidaEntity partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new NotFoundException("Partida não encontrada"));

        if (Boolean.TRUE.equals(partida.getFinalizada())) {
            throw new RuntimeException("Partida já finalizada");
        }

        boolean isCampus1 = dto.getCampus().equalsIgnoreCase(partida.getCampus1().getNome());
        boolean isCampus2 = dto.getCampus().equalsIgnoreCase(partida.getCampus2().getNome());

        if (!isCampus1 && !isCampus2) {
            throw new RuntimeException("O campus informado não pertence a esta partida");
        }

        CampusEntity campus = campusService.findByNome(dto.getCampus());

        EstatisticaEntity estCampus = estatisticaRepository
                .findByPartida_IdAndCampus_NomeAndAlunoIsNull(partidaId, dto.getCampus())
                .orElseGet(() -> EstatisticaEntity.builder().campus(campus).partida(partida).build());

        aplicarIncrementos(estCampus, dto);
        estatisticaRepository.save(estCampus);

        String nomeAluno = "Geral Campus";
        if (dto.getAlunoId() != null) {
            EstatisticaEntity estAluno = estatisticaRepository
                    .findByPartida_IdAndCampus_NomeAndAluno_Id(partidaId, dto.getCampus(), dto.getAlunoId())
                    .orElseGet(() -> {
                        AlunoEntity aluno = alunoRepository.findById(dto.getAlunoId())
                                .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));
                        return EstatisticaEntity.builder().campus(campus).partida(partida).aluno(aluno).build();
                    });

            aplicarIncrementos(estAluno, dto);
            nomeAluno = estAluno.getAluno().getNome();
            estatisticaRepository.save(estAluno);
        }

        int pontosGanhos = (coalesce(dto.getBolas2()) * 2)
                + (coalesce(dto.getBolas3()) * 3)
                + coalesce(dto.getLancesLivres());

        if (isCampus1) {
            partida.setPlacarCampus1(partida.getPlacarCampus1() + pontosGanhos);
        } else {
            partida.setPlacarCampus2(partida.getPlacarCampus2() + pontosGanhos);
        }
        partidaRepository.save(partida);

        return EstatisticaResponseDto.builder()
                .campus(dto.getCampus())
                .alunoNome(nomeAluno)
                .bolas2(dto.getBolas2())
                .bolas3(dto.getBolas3())
                .rebotes(dto.getRebotes())
                .assistencias(dto.getAssistencias())
                .lancesLivres(dto.getLancesLivres())
                .faltas(dto.getFaltas())
                .totalPontosNoLance(pontosGanhos)
                .build();
    }

    @Transactional
    public PartidaEntity finalizarPartida(UUID partidaId) {
        PartidaEntity partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new NotFoundException("Partida não encontrada"));

        if (Boolean.TRUE.equals(partida.getFinalizada())) {
            throw new RuntimeException("Partida já finalizada");
        }

        if (partida.getPlacarCampus1() > partida.getPlacarCampus2()) {
            partida.setVencedor(partida.getCampus1().getNome());
        } else if (partida.getPlacarCampus2() > partida.getPlacarCampus1()) {
            partida.setVencedor(partida.getCampus2().getNome());
        } else {
            
            if (partida.getRodada() != TipoRodada.GRUPO) {
                throw new RuntimeException("Partidas eliminatórias não podem terminar em empate");
            }
            partida.setVencedor("EMPATE");
        }

        partida.setFinalizada(true);
        return partidaRepository.save(partida);
    }

    private void aplicarIncrementos(EstatisticaEntity entity, PlacarDto dto) {
        entity.setBolas2(coalesce(entity.getBolas2()) + coalesce(dto.getBolas2()));
        entity.setBolas3(coalesce(entity.getBolas3()) + coalesce(dto.getBolas3()));
        entity.setLancesLivres(coalesce(entity.getLancesLivres()) + coalesce(dto.getLancesLivres()));
        entity.setRebotes(coalesce(entity.getRebotes()) + coalesce(dto.getRebotes()));
        entity.setAssistencias(coalesce(entity.getAssistencias()) + coalesce(dto.getAssistencias()));
        entity.setFaltas(coalesce(entity.getFaltas()) + coalesce(dto.getFaltas()));
        entity.setCestas(entity.getBolas2() + entity.getBolas3());
    }

    private Integer coalesce(Integer val) {
        return val == null ? 0 : val;
    }
}