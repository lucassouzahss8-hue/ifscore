package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.database.model.*;
import com.br.lukisDEV.ifscore.database.repository.*;
import com.br.lukisDEV.ifscore.dto.EstatisticaDto;
import com.br.lukisDEV.ifscore.dto.ModalidadeDto;
import com.br.lukisDEV.ifscore.dto.PlacarDto;
import com.br.lukisDEV.ifscore.enums.TipoRodada;
import com.br.lukisDEV.ifscore.exception.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ModalidadeService {

    private final IEventoRepository eventoRepository;
    private final IModalidadeRepository modalidadeRepository;
    private final IPartidaRepository partidaRepository;
    private final IEstatisticaRepository estatisticaRepository;
    private final IAlunoRepository alunoRepository;
    private final CampusService campusService;

    public List<ModalidadeEntity> findAll() {
        return modalidadeRepository.findAll();
    }

    @Transactional
    public ModalidadeEntity criarModalidade(ModalidadeDto dto) {

        EventoEntity evento = eventoRepository.findById(dto.getEventoId())
                .orElseThrow(() -> new NotFoundException("Evento não encontrado"));

        if (dto.getCampus() != null) {
            dto.getCampus().forEach(campusService::validarCampus);
        }

        ModalidadeEntity modalidade = ModalidadeEntity.builder()
                .nome(dto.getNome())
                .evento(evento)
                .campus(dto.getCampus())
                .partidas(new ArrayList<>())
                .build();

        List<String> campusList = new ArrayList<>(dto.getCampus());
        Collections.shuffle(campusList);

        List<String> chaveA = new ArrayList<>();
        List<String> chaveB = new ArrayList<>();

        for (int i = 0; i < campusList.size(); i++) {
            if (i % 2 == 0) chaveA.add(campusList.get(i));
            else chaveB.add(campusList.get(i));
        }

        modalidade = modalidadeRepository.save(modalidade);

        List<PartidaEntity> partidasList = new ArrayList<>();
        partidasList.addAll(gerarPartidas(chaveA, "A", modalidade));
        partidasList.addAll(gerarPartidas(chaveB, "B", modalidade));

        partidaRepository.saveAll(partidasList);

        return modalidade;
    }

    private List<PartidaEntity> gerarPartidas(List<String> times, String chave, ModalidadeEntity modalidade) {

        List<PartidaEntity> lista = new ArrayList<>();

        for (int i = 0; i < times.size(); i++) {
            for (int j = i + 1; j < times.size(); j++) {

                PartidaEntity p = PartidaEntity.builder()
                        .campus1(times.get(i))
                        .campus2(times.get(j))
                        .chave(chave)
                        .rodada(TipoRodada.GRUPO)
                        .modalidade(modalidade)
                        .placarCampus1(0)
                        .placarCampus2(0)
                        .finalizada(false)
                        .build();

                lista.add(p);
            }
        }

        return lista;
    }

    public List<PartidaEntity> listarPartidas(UUID modalidadeId) {
        return partidaRepository.findByModalidadeId(modalidadeId);
    }

    public Map<String, List<String>> classificacao(UUID modalidadeId) {

        List<PartidaEntity> partidas = listarPartidas(modalidadeId);

        return Map.of(
                "chaveA", classificarPorChave(partidas, "A"),
                "chaveB", classificarPorChave(partidas, "B")
        );
    }

    private List<String> classificarPorChave(List<PartidaEntity> partidas, String chave) {
        Map<String, Integer> vitorias = new HashMap<>();
        Map<String, Integer> saldo = new HashMap<>();

        for (PartidaEntity p : partidas) {

            if (!chave.equals(p.getChave())) continue;
            if (p.getFinalizada() == null || !p.getFinalizada()) continue;

            String c1 = p.getCampus1();
            String c2 = p.getCampus2();

            vitorias.putIfAbsent(c1, 0);
            vitorias.putIfAbsent(c2, 0);

            saldo.putIfAbsent(c1, 0);
            saldo.putIfAbsent(c2, 0);

            int p1 = p.getPlacarCampus1();
            int p2 = p.getPlacarCampus2();

            if (p1 > p2) vitorias.put(c1, vitorias.get(c1) + 1);
            else if (p2 > p1) vitorias.put(c2, vitorias.get(c2) + 1);

            saldo.put(c1, saldo.get(c1) + (p1 - p2));
            saldo.put(c2, saldo.get(c2) + (p2 - p1));
        }

        List<String> ranking = new ArrayList<>(vitorias.keySet());

        ranking.sort((a, b) -> {
            int comp = Integer.compare(vitorias.get(b), vitorias.get(a));
            if (comp != 0) return comp;
            return Integer.compare(saldo.get(b), saldo.get(a));
        });

        return ranking;
    }

    public Map<String, List<Map<String, Object>>> classificacaoCompleta(UUID modalidadeId) {

        List<PartidaEntity> partidas = listarPartidas(modalidadeId);

        return Map.of(
                "chaveA", gerarTabela(partidas, "A"),
                "chaveB", gerarTabela(partidas, "B")
        );
    }

    private List<Map<String, Object>> gerarTabela(List<PartidaEntity> partidas, String chave) {

        Map<String, Integer> jogos = new HashMap<>();
        Map<String, Integer> vitorias = new HashMap<>();
        Map<String, Integer> derrotas = new HashMap<>();
        Map<String, Integer> pontosPro = new HashMap<>();
        Map<String, Integer> pontosContra = new HashMap<>();

        for (PartidaEntity p : partidas) {

            if (!chave.equals(p.getChave())) continue;
            if (p.getFinalizada() == null || !p.getFinalizada()) continue;

            String c1 = p.getCampus1();
            String c2 = p.getCampus2();

            jogos.put(c1, jogos.getOrDefault(c1, 0) + 1);
            jogos.put(c2, jogos.getOrDefault(c2, 0) + 1);

            pontosPro.put(c1, pontosPro.getOrDefault(c1, 0) + p.getPlacarCampus1());
            pontosContra.put(c1, pontosContra.getOrDefault(c1, 0) + p.getPlacarCampus2());

            pontosPro.put(c2, pontosPro.getOrDefault(c2, 0) + p.getPlacarCampus2());
            pontosContra.put(c2, pontosContra.getOrDefault(c2, 0) + p.getPlacarCampus1());

            if (p.getPlacarCampus1() > p.getPlacarCampus2()) {
                vitorias.put(c1, vitorias.getOrDefault(c1, 0) + 1);
                derrotas.put(c2, derrotas.getOrDefault(c2, 0) + 1);
            } else if (p.getPlacarCampus2() > p.getPlacarCampus1()) {
                vitorias.put(c2, vitorias.getOrDefault(c2, 0) + 1);
                derrotas.put(c1, derrotas.getOrDefault(c1, 0) + 1);
            }
        }

        List<Map<String, Object>> tabela = new ArrayList<>();

        for (String time : jogos.keySet()) {

            int saldo = pontosPro.get(time) - pontosContra.get(time);

            tabela.add(Map.of(
                    "time", time,
                    "jogos", jogos.get(time),
                    "vitorias", vitorias.getOrDefault(time, 0),
                    "derrotas", derrotas.getOrDefault(time, 0),
                    "pontosPro", pontosPro.get(time),
                    "pontosContra", pontosContra.get(time),
                    "saldo", saldo,
                    "pontos", vitorias.getOrDefault(time, 0) * 3
            ));
        }

        tabela.sort((a, b) -> {
            int p = Integer.compare((int) b.get("pontos"), (int) a.get("pontos"));
            if (p != 0) return p;

            return Integer.compare((int) b.get("saldo"), (int) a.get("saldo"));
        });

        return tabela;
    }

    @Transactional
    public List<PartidaEntity> gerarSemifinal(UUID modalidadeId) {

        List<PartidaEntity> partidas = listarPartidas(modalidadeId);

        if (partidas.stream().filter(p -> p.getRodada() == TipoRodada.GRUPO).anyMatch(p -> !p.getFinalizada())) {
            throw new RuntimeException("Finalize os jogos do grupo primeiro");
        }

        List<String> chaveA = classificarPorChave(partidas, "A");
        List<String> chaveB = classificarPorChave(partidas, "B");

        if (chaveA.size() < 2 || chaveB.size() < 2) {
            throw new RuntimeException("Não há times suficientes para gerar a semifinal (mínimo 2 por chave)");
        }

        List<PartidaEntity> semi = new ArrayList<>();

        semi.add(criarJogo(chaveA.get(0), chaveB.get(1), modalidadeId, TipoRodada.SEMIFINAL));
        semi.add(criarJogo(chaveB.get(0), chaveA.get(1), modalidadeId, TipoRodada.SEMIFINAL));

        partidaRepository.saveAll(semi);

        return semi;
    }

    @Transactional
    public PartidaEntity gerarFinal(UUID modalidadeId) {

        List<PartidaEntity> partidas = listarPartidas(modalidadeId);

        List<PartidaEntity> semis = partidas.stream()
                .filter(p -> p.getRodada() == TipoRodada.SEMIFINAL)
                .toList();

        if (semis.size() < 2) {
            throw new RuntimeException("As duas semifinais devem existir antes de gerar a final");
        }

        if (semis.stream().anyMatch(p -> p.getVencedor() == null)) {
            throw new RuntimeException("Finalize as semifinais");
        }

        PartidaEntity finalJogo = criarJogo(
                semis.get(0).getVencedor(),
                semis.get(1).getVencedor(),
                modalidadeId,
                TipoRodada.FINAL
        );

        return partidaRepository.save(finalJogo);
    }

    private PartidaEntity criarJogo(String c1, String c2, UUID modalidadeId, TipoRodada rodada) {

        ModalidadeEntity modalidade = modalidadeRepository.findById(modalidadeId)
                .orElseThrow();

        return PartidaEntity.builder()
                .campus1(c1)
                .campus2(c2)
                .modalidade(modalidade)
                .rodada(rodada)
                .placarCampus1(0)
                .placarCampus2(0)
                .finalizada(false)
                .build();
    }

    @Transactional
    public PartidaEntity somarPontos(UUID partidaId, PlacarDto dto) {

        PartidaEntity partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new RuntimeException("Partida não encontrada"));

        if (Boolean.TRUE.equals(partida.getFinalizada())) {
            throw new RuntimeException("Partida já finalizada");
        }

        if (dto.getTime().equalsIgnoreCase("campus1")) {
            partida.setPlacarCampus1(partida.getPlacarCampus1() + dto.getPontos());
        } else if (dto.getTime().equalsIgnoreCase("campus2")) {
            partida.setPlacarCampus2(partida.getPlacarCampus2() + dto.getPontos());
        } else {
            throw new RuntimeException("Time inválido");
        }

        return partidaRepository.save(partida);
    }

    @Transactional
    public PartidaEntity finalizarPartida(UUID partidaId) {

        PartidaEntity partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new NotFoundException("Partida não encontrada"));

        if (Boolean.TRUE.equals(partida.getFinalizada())) {
            throw new RuntimeException("Partida já finalizada");
        }

        if (partida.getPlacarCampus1() > partida.getPlacarCampus2()) {
            partida.setVencedor(partida.getCampus1());
        } else if (partida.getPlacarCampus2() > partida.getPlacarCampus1()) {
            partida.setVencedor(partida.getCampus2());
        } else {
            partida.setVencedor("EMPATE");
        }

        partida.setFinalizada(true);

        return partidaRepository.save(partida);
    }

    @Transactional
    public EstatisticaEntity atualizarEstatisticas(
            UUID partidaId,
            EstatisticaDto dto
    ) {

        PartidaEntity partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new NotFoundException("Partida não encontrada"));

        campusService.validarCampus(dto.getCampus());

        EstatisticaEntity est;

        if (dto.getAlunoId() != null) {
            est = estatisticaRepository
                    .findByPartida_IdAndCampusAndAluno_Id(partidaId, dto.getCampus(), dto.getAlunoId())
                    .orElseGet(() -> {
                        AlunoEntity aluno = alunoRepository.findById(dto.getAlunoId())
                                .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));
                        return EstatisticaEntity.builder()
                                .campus(dto.getCampus())
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
                    });
        } else {
            est = estatisticaRepository
                    .findByPartida_IdAndCampus(partidaId, dto.getCampus())
                    .orElseGet(() -> EstatisticaEntity.builder()
                                    .campus(dto.getCampus())
                                    .partida(partida)
                                    .cestas(0)
                                    .bolas2(0)
                                    .bolas3(0)
                                    .rebotes(0)
                                    .assistencias(0)
                                    .lancesLivres(0)
                                    .faltas(0)
                                    .build()
                    );
        }

        est.setBolas2(est.getBolas2() + (dto.getBolas2() != null ? dto.getBolas2() : 0));
        est.setBolas3(est.getBolas3() + (dto.getBolas3() != null ? dto.getBolas3() : 0));
        est.setCestas(est.getBolas2() + est.getBolas3());
        est.setLancesLivres(est.getLancesLivres() + (dto.getLancesLivres() != null ? dto.getLancesLivres() : 0));
        est.setFaltas(est.getFaltas() + (dto.getFaltas() != null ? dto.getFaltas() : 0));
        est.setRebotes(est.getRebotes() + (dto.getRebotes() != null ? dto.getRebotes() : 0));
        est.setAssistencias(est.getAssistencias() + (dto.getAssistencias() != null ? dto.getAssistencias() : 0));

        int pontos = ((dto.getBolas2() != null ? dto.getBolas2() : 0) * 2)
                + ((dto.getBolas3() != null ? dto.getBolas3() : 0) * 3)
                + (dto.getLancesLivres() != null ? dto.getLancesLivres() : 0);

        if (dto.getCampus().equals(partida.getCampus1())) {
            partida.setPlacarCampus1(partida.getPlacarCampus1() + pontos);
        } else {
            partida.setPlacarCampus2(partida.getPlacarCampus2() + pontos);
        }

        partidaRepository.save(partida);

        return estatisticaRepository.save(est);
    }

    public void delete(UUID id) {
        if (!modalidadeRepository.existsById(id)) {
            throw new NotFoundException("Modalidade não existe");
        }
        modalidadeRepository.deleteById(id);
    }
}