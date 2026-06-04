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

        CampusEntity campusAlvo = null;
        String inputCampus = dto.getCampus().trim();

        // Tenta identificar o campus por ID ou Nome
        if (isUUID(inputCampus)) {
            UUID campusId = UUID.fromString(inputCampus);
            if (partida.getCampus1() != null && partida.getCampus1().getId().equals(campusId)) {
                campusAlvo = partida.getCampus1();
            } else if (partida.getCampus2() != null && partida.getCampus2().getId().equals(campusId)) {
                campusAlvo = partida.getCampus2();
            }
        } else {
            if (partida.getCampus1() != null && inputCampus.equalsIgnoreCase(partida.getCampus1().getNome())) {
                campusAlvo = partida.getCampus1();
            } else if (partida.getCampus2() != null && inputCampus.equalsIgnoreCase(partida.getCampus2().getNome())) {
                campusAlvo = partida.getCampus2();
            }
        }

        if (campusAlvo == null) {
            throw new RuntimeException("O campus informado ('" + inputCampus + "') não pertence a esta partida.");
        }

        final CampusEntity campus = campusAlvo;

        EstatisticaEntity estCampus = estatisticaRepository
                .findByPartida_IdAndCampus_NomeAndAlunoIsNull(partidaId, campus.getNome())
                .orElseGet(() -> EstatisticaEntity.builder().campus(campus).partida(partida).build());

        aplicarIncrementos(estCampus, dto);
        estatisticaRepository.save(estCampus);

        EstatisticaEntity estFinal = estCampus;
        String nomeAluno = "Geral Campus";
        if (dto.getAlunoId() != null) {
            EstatisticaEntity estAluno = estatisticaRepository
                    .findByPartida_IdAndCampus_NomeAndAluno_Id(partidaId, campus.getNome(), dto.getAlunoId())
                    .orElseGet(() -> {
                        AlunoEntity aluno = alunoRepository.findById(dto.getAlunoId())
                                .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));
                        return EstatisticaEntity.builder().campus(campus).partida(partida).aluno(aluno).build();
                    });

            aplicarIncrementos(estAluno, dto);
            nomeAluno = estAluno.getAluno().getNome();
            estatisticaRepository.save(estAluno);
            estFinal = estAluno;
        }

        int pontosGanhos = (coalesce(dto.getBolas2()) * 2)
                + (coalesce(dto.getBolas3()) * 3)
                + coalesce(dto.getLancesLivres());

        if (campus.getId().equals(partida.getCampus1().getId())) {
            partida.setPlacarCampus1(coalesce(partida.getPlacarCampus1()) + pontosGanhos);
        } else {
            partida.setPlacarCampus2(coalesce(partida.getPlacarCampus2()) + pontosGanhos);
        }
        partidaRepository.save(partida);

        return EstatisticaResponseDto.builder()
                .campus(campus.getNome())
                .alunoNome(nomeAluno)
                .bolas2(estFinal.getBolas2())
                .bolas3(estFinal.getBolas3())
                .rebotes(estFinal.getRebotes())
                .assistencias(estFinal.getAssistencias())
                .lancesLivres(estFinal.getLancesLivres())
                .faltas(estFinal.getFaltas())
                .roubos(estFinal.getRoubos())
                .tocos(estFinal.getTocos())
                .totalPontosNoLance(pontosGanhos)
                .build();
    }

    private boolean isUUID(String str) {
        try {
            UUID.fromString(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Transactional
    public PartidaEntity finalizarPartida(UUID partidaId) {
        PartidaEntity partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new NotFoundException("Partida não encontrada"));

        if (Boolean.TRUE.equals(partida.getFinalizada())) {
            throw new RuntimeException("Partida já finalizada");
        }

        int placar1 = coalesce(partida.getPlacarCampus1());
        int placar2 = coalesce(partida.getPlacarCampus2());

        if (placar1 > placar2) {
            partida.setVencedor(partida.getCampus1() != null ? partida.getCampus1().getNome() : "DESCONHECIDO");
        } else if (placar2 > placar1) {
            partida.setVencedor(partida.getCampus2() != null ? partida.getCampus2().getNome() : "DESCONHECIDO");
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