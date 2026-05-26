package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.database.model.PartidaEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModalidadeClassificacaoService {

    public Map<String, List<String>> classificacao(List<PartidaEntity> partidas) {
        return Map.of(
                "chaveA", classificarPorChave(partidas, "A"),
                "chaveB", classificarPorChave(partidas, "B")
        );
    }

    public Map<String, List<Map<String, Object>>> classificacaoCompleta(List<PartidaEntity> partidas) {
        return Map.of(
                "chaveA", gerarTabela(partidas, "A"),
                "chaveB", gerarTabela(partidas, "B")
        );
    }

    public List<String> classificarPorChave(List<PartidaEntity> partidas, String chave) {
        Map<String, Integer> vitorias = new HashMap<>();
        Map<String, Integer> saldo = new HashMap<>();

        for (PartidaEntity partida : partidas) {
            if (!chave.equals(partida.getChave())) continue;
            if (partida.getFinalizada() == null || !partida.getFinalizada()) continue;

            String campus1 = partida.getCampus1().getNome();
            String campus2 = partida.getCampus2().getNome();

            vitorias.putIfAbsent(campus1, 0);
            vitorias.putIfAbsent(campus2, 0);
            saldo.putIfAbsent(campus1, 0);
            saldo.putIfAbsent(campus2, 0);

            int placar1 = partida.getPlacarCampus1();
            int placar2 = partida.getPlacarCampus2();

            if (placar1 > placar2) vitorias.put(campus1, vitorias.get(campus1) + 1);
            else if (placar2 > placar1) vitorias.put(campus2, vitorias.get(campus2) + 1);

            saldo.put(campus1, saldo.get(campus1) + (placar1 - placar2));
            saldo.put(campus2, saldo.get(campus2) + (placar2 - placar1));
        }

        List<String> ranking = new ArrayList<>(vitorias.keySet());
        ranking.sort((a, b) -> {
            int comp = Integer.compare(vitorias.get(b), vitorias.get(a));
            if (comp != 0) return comp;
            return Integer.compare(saldo.get(b), saldo.get(a));
        });

        return ranking;
    }

    private List<Map<String, Object>> gerarTabela(List<PartidaEntity> partidas, String chave) {
        Map<String, Integer> jogos = new HashMap<>();
        Map<String, Integer> vitorias = new HashMap<>();
        Map<String, Integer> derrotas = new HashMap<>();
        Map<String, Integer> pontosPro = new HashMap<>();
        Map<String, Integer> pontosContra = new HashMap<>();

        for (PartidaEntity partida : partidas) {
            if (!chave.equals(partida.getChave())) continue;
            if (partida.getFinalizada() == null || !partida.getFinalizada()) continue;

            String campus1 = partida.getCampus1().getNome();
            String campus2 = partida.getCampus2().getNome();

            jogos.put(campus1, jogos.getOrDefault(campus1, 0) + 1);
            jogos.put(campus2, jogos.getOrDefault(campus2, 0) + 1);

            pontosPro.put(campus1, pontosPro.getOrDefault(campus1, 0) + partida.getPlacarCampus1());
            pontosContra.put(campus1, pontosContra.getOrDefault(campus1, 0) + partida.getPlacarCampus2());

            pontosPro.put(campus2, pontosPro.getOrDefault(campus2, 0) + partida.getPlacarCampus2());
            pontosContra.put(campus2, pontosContra.getOrDefault(campus2, 0) + partida.getPlacarCampus1());

            if (partida.getPlacarCampus1() > partida.getPlacarCampus2()) {
                vitorias.put(campus1, vitorias.getOrDefault(campus1, 0) + 1);
                derrotas.put(campus2, derrotas.getOrDefault(campus2, 0) + 1);
            } else if (partida.getPlacarCampus2() > partida.getPlacarCampus1()) {
                vitorias.put(campus2, vitorias.getOrDefault(campus2, 0) + 1);
                derrotas.put(campus1, derrotas.getOrDefault(campus1, 0) + 1);
            }
        }

        List<Map<String, Object>> tabela = new ArrayList<>();

        for (String time : jogos.keySet()) {
            int saldo = pontosPro.get(time) - pontosContra.get(time);

            int pontos = 0;
            if (vitorias.getOrDefault(time, 0) > 0 || derrotas.getOrDefault(time, 0) > 0) {

                pontos = (vitorias.getOrDefault(time, 0) * 2) + derrotas.getOrDefault(time, 0);

                int totalJogos = jogos.getOrDefault(time, 0);
                int jogosComResultado = vitorias.getOrDefault(time, 0) + derrotas.getOrDefault(time, 0);
                int empates = totalJogos - jogosComResultado;
                pontos += empates; 
            }

            tabela.add(Map.of(
                    "time", time,
                    "jogos", jogos.get(time),
                    "vitorias", vitorias.getOrDefault(time, 0),
                    "derrotas", derrotas.getOrDefault(time, 0),
                    "pontosPro", pontosPro.get(time),
                    "pontosContra", pontosContra.get(time),
                    "saldo", saldo,
                    "pontos", pontos
            ));
        }

        tabela.sort((a, b) -> {
            int pontos = Integer.compare((int) b.get("pontos"), (int) a.get("pontos"));
            if (pontos != 0) return pontos;
            return Integer.compare((int) b.get("saldo"), (int) a.get("saldo"));
        });

        return tabela;
    }
}
