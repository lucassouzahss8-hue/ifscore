package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.database.model.AlunoEntity;
import com.br.lukisDEV.ifscore.database.model.EstatisticaEntity;
import com.br.lukisDEV.ifscore.database.model.ModalidadeEntity;
import com.br.lukisDEV.ifscore.database.repository.IAlunoRepository;
import com.br.lukisDEV.ifscore.database.repository.IEstatisticaRepository;
import com.br.lukisDEV.ifscore.database.repository.IModalidadeRepository;
import com.br.lukisDEV.ifscore.dto.AlunoDto;
import com.br.lukisDEV.ifscore.dto.AlunoPerfilDto;
import com.br.lukisDEV.ifscore.exception.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
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

    // =========================
    // 📋 PERFIL
    // =========================
    public AlunoPerfilDto getAlunoPerfil(UUID id) {
        AlunoEntity aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado"));

        List<EstatisticaEntity> estatisticas = estatisticaRepository.findByAluno_Id(id);

        int totalPontos = estatisticas.stream()
                .mapToInt(e -> {
                    int p2 = e.getBolas2() != null ? e.getBolas2() : 0;
                    int p3 = e.getBolas3() != null ? e.getBolas3() : 0;
                    int ll = e.getLancesLivres() != null ? e.getLancesLivres() : 0;
                    return (p2 * 2) + (p3 * 3) + ll;
                })
                .sum();

        return AlunoPerfilDto.builder()
                .nome(aluno.getNome())
                .campus(aluno.getCampus())
                .numeroRegata(aluno.getNumeroRegata())
                .pontuacao(totalPontos)
                .build();
    }

    // =========================
    // 📋 LISTAR
    // =========================
    public List<AlunoEntity> findAll() {
        return alunoRepository.findAll();
    }

    // =========================
    // 💾 CRIAR
    // =========================
    @Transactional
    public AlunoEntity save(AlunoDto dto) {

        campusService.validarCampus(dto.getCampus());

        Set<ModalidadeEntity> modalidades = new HashSet<>();

        if (dto.getModalidadesIds() != null) {
            modalidades = new HashSet<>(modalidadeRepository.findAllById(dto.getModalidadesIds()));
        }

        AlunoEntity aluno = AlunoEntity.builder()
                .nome(dto.getNome())
                .campus(dto.getCampus())
                .numeroRegata(dto.getNumeroRegata())
                .modalidades(modalidades)
                .build();

        return alunoRepository.save(aluno);
    }

    // =========================
    // 🔄 ATUALIZAR
    // =========================
    @Transactional
    public AlunoEntity updateAluno(UUID id, AlunoDto dto) {

        campusService.validarCampus(dto.getCampus());

        AlunoEntity aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));

        aluno.setNome(dto.getNome());
        aluno.setCampus(dto.getCampus());
        aluno.setNumeroRegata(dto.getNumeroRegata());

        if (dto.getModalidadesIds() != null) {
            Set<ModalidadeEntity> modalidades =
                    new HashSet<>(modalidadeRepository.findAllById(dto.getModalidadesIds()));

            aluno.setModalidades(modalidades);
        }

        return alunoRepository.save(aluno);
    }

    // =========================
    // ❌ DELETE
    // =========================
    @Transactional
    public void deleteAluno(UUID id) {

        if (!alunoRepository.existsById(id)) {
            throw new EntityNotFoundException("Aluno não encontrado");
        }

        alunoRepository.deleteById(id);
    }
}