package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.database.model.AlunoEntity;
import com.br.lukisDEV.ifscore.database.model.CampusEntity;
import com.br.lukisDEV.ifscore.database.model.EstatisticaEntity;
import com.br.lukisDEV.ifscore.database.model.PartidaEntity;
import com.br.lukisDEV.ifscore.database.repository.IAlunoRepository;
import com.br.lukisDEV.ifscore.database.repository.IEstatisticaRepository;
import com.br.lukisDEV.ifscore.database.repository.IPartidaRepository;
import com.br.lukisDEV.ifscore.dto.EstatisticaDto;
import com.br.lukisDEV.ifscore.dto.PlacarDto;
import com.br.lukisDEV.ifscore.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ModalidadePlacarService {

    private final IPartidaRepository partidaRepository;
    private final IEstatisticaRepository estatisticaRepository;
    private final IAlunoRepository alunoRepository;
    private final CampusService campusService;

    public PartidaEntity somarPontos(UUID partidaId, PlacarDto dto) {
        PartidaEntity partida = buscarPartida(partidaId);

        validarPartidaAberta(partida);

        if ("campus1".equalsIgnoreCase(dto.getTime())) {
            partida.setPlacarCampus1(partida.getPlacarCampus1() + dto.getPontos());
        } else if ("campus2".equalsIgnoreCase(dto.getTime())) {
            partida.setPlacarCampus2(partida.getPlacarCampus2() + dto.getPontos());
        } else {
            throw new IllegalArgumentException("Time invalido");
        }

        return partidaRepository.save(partida);
    }

    public PartidaEntity finalizarPartida(UUID partidaId) {
        PartidaEntity partida = buscarPartida(partidaId);

        validarPartidaAberta(partida);

        if (partida.getPlacarCampus1() > partida.getPlacarCampus2()) {
            partida.setVencedor(partida.getCampus1().getNome());
        } else if (partida.getPlacarCampus2() > partida.getPlacarCampus1()) {
            partida.setVencedor(partida.getCampus2().getNome());
        } else {
            partida.setVencedor("EMPATE");
        }

        partida.setFinalizada(true);

        return partidaRepository.save(partida);
    }

    public EstatisticaEntity atualizarEstatisticas(UUID partidaId, EstatisticaDto dto) {
        PartidaEntity partida = buscarPartida(partidaId);
        CampusEntity campus = campusService.findByNome(dto.getCampus());

        validarCampusNaPartida(partida, dto.getCampus());

        EstatisticaEntity estatistica = buscarOuCriarEstatistica(partida, campus, dto);

        estatistica.setBolas2(estatistica.getBolas2() + valor(dto.getBolas2()));
        estatistica.setBolas3(estatistica.getBolas3() + valor(dto.getBolas3()));
        estatistica.setCestas(estatistica.getBolas2() + estatistica.getBolas3());
        estatistica.setLancesLivres(estatistica.getLancesLivres() + valor(dto.getLancesLivres()));
        estatistica.setFaltas(estatistica.getFaltas() + valor(dto.getFaltas()));
        estatistica.setRebotes(estatistica.getRebotes() + valor(dto.getRebotes()));
        estatistica.setAssistencias(estatistica.getAssistencias() + valor(dto.getAssistencias()));

        int pontos = (valor(dto.getBolas2()) * 2)
                + (valor(dto.getBolas3()) * 3)
                + valor(dto.getLancesLivres());

        if (dto.getCampus().equals(partida.getCampus1().getNome())) {
            partida.setPlacarCampus1(partida.getPlacarCampus1() + pontos);
        } else {
            partida.setPlacarCampus2(partida.getPlacarCampus2() + pontos);
        }

        partidaRepository.save(partida);

        return estatisticaRepository.save(estatistica);
    }

    private EstatisticaEntity buscarOuCriarEstatistica(PartidaEntity partida, CampusEntity campus, EstatisticaDto dto) {
        if (dto.getAlunoId() != null) {
            return estatisticaRepository
                    .findByPartida_IdAndCampus_NomeAndAluno_Id(partida.getId(), dto.getCampus(), dto.getAlunoId())
                    .orElseGet(() -> {
                        AlunoEntity aluno = alunoRepository.findById(dto.getAlunoId())
                                .orElseThrow(() -> new NotFoundException("Aluno nao encontrado"));
                        return novaEstatistica(partida, campus, aluno);
                    });
        }

        return estatisticaRepository
                .findByPartida_IdAndCampus_Nome(partida.getId(), dto.getCampus())
                .orElseGet(() -> novaEstatistica(partida, campus, null));
    }

    private EstatisticaEntity novaEstatistica(PartidaEntity partida, CampusEntity campus, AlunoEntity aluno) {
        return EstatisticaEntity.builder()
                .campus(campus)
                .partida(partida)
                .aluno(aluno)
                .cestas(0)
                .bolas2(0)
                .bolas3(0)
                .rebotes(0)
                .assistencias(0)
                .lancesLivres(0)
                .faltas(0)
                .build();
    }

    private PartidaEntity buscarPartida(UUID partidaId) {
        return partidaRepository.findById(partidaId)
                .orElseThrow(() -> new NotFoundException("Partida nao encontrada"));
    }

    private void validarPartidaAberta(PartidaEntity partida) {
        if (Boolean.TRUE.equals(partida.getFinalizada())) {
            throw new IllegalStateException("Partida ja finalizada");
        }
    }

    private void validarCampusNaPartida(PartidaEntity partida, String campus) {
        boolean campus1 = campus.equals(partida.getCampus1().getNome());
        boolean campus2 = campus.equals(partida.getCampus2().getNome());

        if (!campus1 && !campus2) {
            throw new IllegalArgumentException("Campus informado nao participa desta partida");
        }
    }

    private int valor(Integer valor) {
        return valor != null ? valor : 0;
    }
}
