package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.database.model.CampusEntity;
import com.br.lukisDEV.ifscore.database.model.EstatisticaEntity;
import com.br.lukisDEV.ifscore.database.model.EventoEntity;
import com.br.lukisDEV.ifscore.database.model.ModalidadeEntity;
import com.br.lukisDEV.ifscore.database.model.PartidaEntity;
import com.br.lukisDEV.ifscore.database.repository.IEventoRepository;
import com.br.lukisDEV.ifscore.database.repository.IModalidadeRepository;
import com.br.lukisDEV.ifscore.database.repository.IPartidaRepository;
import com.br.lukisDEV.ifscore.dto.EstatisticaDto;
import com.br.lukisDEV.ifscore.dto.ModalidadeDto;
import com.br.lukisDEV.ifscore.dto.PlacarDto;
import com.br.lukisDEV.ifscore.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ModalidadeService {

    private final IEventoRepository eventoRepository;
    private final IModalidadeRepository modalidadeRepository;
    private final IPartidaRepository partidaRepository;
    private final CampusService campusService;
    private final ModalidadeClassificacaoService classificacaoService;
    private final ModalidadeChaveamentoService chaveamentoService;
    private final ModalidadePlacarService placarService;

    public List<ModalidadeEntity> findAll() {
        return modalidadeRepository.findAll();
    }

    @Transactional
    public ModalidadeEntity criarModalidade(ModalidadeDto dto) {
        EventoEntity evento = eventoRepository.findById(dto.getEventoId())
                .orElseThrow(() -> new NotFoundException("Evento nao encontrado"));

        List<CampusEntity> campusEntities = dto.getCampus().stream()
                .map(campusService::findByNome)
                .toList();

        ModalidadeEntity modalidade = ModalidadeEntity.builder()
                .nome(dto.getNome())
                .evento(evento)
                .campus(campusEntities)
                .partidas(new ArrayList<>())
                .build();

        modalidade = modalidadeRepository.save(modalidade);

        List<PartidaEntity> partidas = chaveamentoService.gerarPartidasDeGrupo(campusEntities, modalidade);
        partidaRepository.saveAll(partidas);

        return modalidade;
    }

    public List<PartidaEntity> listarPartidas(UUID modalidadeId) {
        return partidaRepository.findByModalidadeId(modalidadeId);
    }

    public Map<String, List<String>> classificacao(UUID modalidadeId) {
        return classificacaoService.classificacao(listarPartidas(modalidadeId));
    }

    public Map<String, List<Map<String, Object>>> classificacaoCompleta(UUID modalidadeId) {
        return classificacaoService.classificacaoCompleta(listarPartidas(modalidadeId));
    }

    @Transactional
    public List<PartidaEntity> gerarSemifinal(UUID modalidadeId) {
        return chaveamentoService.gerarSemifinal(modalidadeId, listarPartidas(modalidadeId));
    }

    @Transactional
    public PartidaEntity gerarFinal(UUID modalidadeId) {
        return chaveamentoService.gerarFinal(modalidadeId, listarPartidas(modalidadeId));
    }

    @Transactional
    public PartidaEntity somarPontos(UUID partidaId, PlacarDto dto) {
        return placarService.somarPontos(partidaId, dto);
    }

    @Transactional
    public PartidaEntity finalizarPartida(UUID partidaId) {
        return placarService.finalizarPartida(partidaId);
    }

    @Transactional
    public EstatisticaEntity atualizarEstatisticas(UUID partidaId, EstatisticaDto dto) {
        return placarService.atualizarEstatisticas(partidaId, dto);
    }

    public void delete(UUID id) {
        if (!modalidadeRepository.existsById(id)) {
            throw new NotFoundException("Modalidade nao existe");
        }
        modalidadeRepository.deleteById(id);
    }
}
