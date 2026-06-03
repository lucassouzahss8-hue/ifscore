package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.database.model.AlunoEntity;
import com.br.lukisDEV.ifscore.database.model.CampusEntity;
import com.br.lukisDEV.ifscore.database.model.EstatisticaEntity;
import com.br.lukisDEV.ifscore.database.model.ModalidadeEntity;
import com.br.lukisDEV.ifscore.database.repository.IAlunoRepository;
import com.br.lukisDEV.ifscore.database.repository.IEstatisticaRepository;
import com.br.lukisDEV.ifscore.database.repository.IModalidadeRepository;
import com.br.lukisDEV.ifscore.dto.AlunoDto;
import com.br.lukisDEV.ifscore.dto.AlunoPerfilDto;
import com.br.lukisDEV.ifscore.dto.AlunoResponseDto;
import com.br.lukisDEV.ifscore.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final IAlunoRepository alunoRepository;
    private final IModalidadeRepository modalidadeRepository;
    private final IEstatisticaRepository estatisticaRepository;
    private final CampusService campusService;

    public AlunoPerfilDto getAlunoPerfil(UUID id) {
        AlunoEntity aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Aluno nao encontrado"));

        List<EstatisticaEntity> estatisticas = estatisticaRepository.findByAlunoId(id);

        int totalPontos = 0;
        int totalCestas = 0;
        int totalBolas2 = 0;
        int totalBolas3 = 0;
        int totalLancesLivres = 0;
        int totalRebotes = 0;
        int totalAssistencias = 0;
        int totalFaltas = 0;
        String campusNome = "N/A";

        if (!estatisticas.isEmpty()) {
            campusNome = estatisticas.get(0).getCampus().getNome();
            for (EstatisticaEntity e : estatisticas) {
                int p2 = e.getBolas2() != null ? e.getBolas2() : 0;
                int p3 = e.getBolas3() != null ? e.getBolas3() : 0;
                int ll = e.getLancesLivres() != null ? e.getLancesLivres() : 0;
                
                totalBolas2 += p2;
                totalBolas3 += p3;
                totalLancesLivres += ll;
                totalPontos += (p2 * 2) + (p3 * 3) + ll;
                totalCestas += (e.getCestas() != null ? e.getCestas() : 0);
                totalRebotes += (e.getRebotes() != null ? e.getRebotes() : 0);
                totalAssistencias += (e.getAssistencias() != null ? e.getAssistencias() : 0);
                totalFaltas += (e.getFaltas() != null ? e.getFaltas() : 0);
            }
        }

        return AlunoPerfilDto.builder()
                .nome(aluno.getNome())
                .campus(campusNome)
                .numeroRegata(aluno.getNumero())
                .pontuacao(totalPontos)
                .cestas(totalCestas)
                .bolas2(totalBolas2)
                .bolas3(totalBolas3)
                .lancesLivres(totalLancesLivres)
                .rebotes(totalRebotes)
                .assistencias(totalAssistencias)
                .faltas(totalFaltas)
                .build();
    }

    public AlunoResponseDto salvarAluno(AlunoDto dto) {
        CampusEntity campus = campusService.findById(dto.getCampusId());
        AlunoEntity aluno = new AlunoEntity();
        aluno.setNome(dto.getNome());
        aluno.setNumero(dto.getNumero());
        aluno.setCampus(campus);
        return AlunoResponseDto.from(alunoRepository.save(aluno));
    }

    public List<AlunoEntity> findAll() {
        return alunoRepository.findAll();
    }

    @Transactional
    public void deleteAluno(UUID id) {

        if (!alunoRepository.existsById(id)) {
            throw new NotFoundException("Aluno nao encontrado");
        }

        alunoRepository.deleteById(id);
    }

    public AlunoResponseDto toResponse(AlunoEntity aluno) {
        return AlunoResponseDto.from(aluno);
    }
}
