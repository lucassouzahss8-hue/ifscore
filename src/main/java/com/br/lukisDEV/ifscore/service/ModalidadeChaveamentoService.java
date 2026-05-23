package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.database.model.CampusEntity;
import com.br.lukisDEV.ifscore.database.model.ModalidadeEntity;
import com.br.lukisDEV.ifscore.database.model.PartidaEntity;
import com.br.lukisDEV.ifscore.database.repository.IModalidadeRepository;
import com.br.lukisDEV.ifscore.database.repository.IPartidaRepository;
import com.br.lukisDEV.ifscore.enums.TipoRodada;
import com.br.lukisDEV.ifscore.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ModalidadeChaveamentoService {

    private final IModalidadeRepository modalidadeRepository;
    private final IPartidaRepository partidaRepository;
    private final CampusService campusService;
    private final ModalidadeClassificacaoService classificacaoService;

    public List<PartidaEntity> gerarPartidasDeGrupo(List<CampusEntity> campus, ModalidadeEntity modalidade) {
        List<CampusEntity> campusShuffle = new ArrayList<>(campus);
        Collections.shuffle(campusShuffle);

        List<CampusEntity> chaveA = new ArrayList<>();
        List<CampusEntity> chaveB = new ArrayList<>();

        for (int i = 0; i < campusShuffle.size(); i++) {
            if (i % 2 == 0) chaveA.add(campusShuffle.get(i));
            else chaveB.add(campusShuffle.get(i));
        }

        List<PartidaEntity> partidas = new ArrayList<>();
        partidas.addAll(gerarPartidas(chaveA, "A", modalidade));
        partidas.addAll(gerarPartidas(chaveB, "B", modalidade));

        return partidas;
    }

    public List<PartidaEntity> gerarSemifinal(UUID modalidadeId, List<PartidaEntity> partidas) {
        if (partidas.stream().anyMatch(partida -> partida.getRodada() == TipoRodada.SEMIFINAL)) {
            throw new IllegalStateException("Semifinais ja foram geradas para esta modalidade");
        }

        if (partidas.stream().filter(partida -> partida.getRodada() == TipoRodada.GRUPO).anyMatch(partida -> !partida.getFinalizada())) {
            throw new IllegalStateException("Finalize os jogos do grupo primeiro");
        }

        List<String> chaveA = classificacaoService.classificarPorChave(partidas, "A");
        List<String> chaveB = classificacaoService.classificarPorChave(partidas, "B");

        if (chaveA.size() < 2 || chaveB.size() < 2) {
            throw new IllegalStateException("Nao ha times suficientes para gerar a semifinal (minimo 2 por chave)");
        }

        List<PartidaEntity> semifinais = new ArrayList<>();
        semifinais.add(criarJogo(chaveA.get(0), chaveB.get(1), modalidadeId, TipoRodada.SEMIFINAL));
        semifinais.add(criarJogo(chaveB.get(0), chaveA.get(1), modalidadeId, TipoRodada.SEMIFINAL));

        return partidaRepository.saveAll(semifinais);
    }

    public PartidaEntity gerarFinal(UUID modalidadeId, List<PartidaEntity> partidas) {
        if (partidas.stream().anyMatch(partida -> partida.getRodada() == TipoRodada.FINAL)) {
            throw new IllegalStateException("Final ja foi gerada para esta modalidade");
        }

        List<PartidaEntity> semifinais = partidas.stream()
                .filter(partida -> partida.getRodada() == TipoRodada.SEMIFINAL)
                .toList();

        if (semifinais.size() < 2) {
            throw new IllegalStateException("As duas semifinais devem existir antes de gerar a final");
        }

        if (semifinais.stream().anyMatch(partida -> partida.getVencedor() == null)) {
            throw new IllegalStateException("Finalize as semifinais");
        }

        if (semifinais.stream().anyMatch(partida -> "EMPATE".equalsIgnoreCase(partida.getVencedor()))) {
            throw new IllegalStateException("Semifinais nao podem terminar empatadas para gerar a final");
        }

        PartidaEntity finalJogo = criarJogo(
                semifinais.get(0).getVencedor(),
                semifinais.get(1).getVencedor(),
                modalidadeId,
                TipoRodada.FINAL
        );

        return partidaRepository.save(finalJogo);
    }

    private List<PartidaEntity> gerarPartidas(List<CampusEntity> times, String chave, ModalidadeEntity modalidade) {
        List<PartidaEntity> partidas = new ArrayList<>();

        for (int i = 0; i < times.size(); i++) {
            for (int j = i + 1; j < times.size(); j++) {
                partidas.add(PartidaEntity.builder()
                        .campus1(times.get(i))
                        .campus2(times.get(j))
                        .chave(chave)
                        .rodada(TipoRodada.GRUPO)
                        .modalidade(modalidade)
                        .placarCampus1(0)
                        .placarCampus2(0)
                        .finalizada(false)
                        .build());
            }
        }

        return partidas;
    }

    private PartidaEntity criarJogo(String campus1Nome, String campus2Nome, UUID modalidadeId, TipoRodada rodada) {
        ModalidadeEntity modalidade = modalidadeRepository.findById(modalidadeId)
                .orElseThrow(() -> new NotFoundException("Modalidade nao encontrada"));

        CampusEntity campus1 = campusService.findByNome(campus1Nome);
        CampusEntity campus2 = campusService.findByNome(campus2Nome);

        return PartidaEntity.builder()
                .campus1(campus1)
                .campus2(campus2)
                .modalidade(modalidade)
                .rodada(rodada)
                .placarCampus1(0)
                .placarCampus2(0)
                .finalizada(false)
                .build();
    }
}
