package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.database.model.CampusEntity;
import com.br.lukisDEV.ifscore.database.repository.ICampusRepository;
import com.br.lukisDEV.ifscore.dto.CampusDto;
import com.br.lukisDEV.ifscore.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CampusService {
    private final ICampusRepository campusRepository;

    public List<CampusEntity> findAll(){
        return campusRepository.findAll();
    }
    public void save(CampusDto campusDto){
        campusRepository.save(CampusEntity.builder()
                .nome(campusDto.getNome())
                .regiao(campusDto.getRegiao())
                .build());
    }
    @Transactional
    public CampusEntity updateCampus(UUID id, CampusDto campusDto) {

        CampusEntity campus = campusRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Campus nao encontrado"));

            campus.setNome(campusDto.getNome());
            campus.setRegiao(campusDto.getRegiao());

        return campusRepository.save(campus);
    }

    @Transactional
    public void deleteCampus(UUID id) {
        if (!campusRepository.existsById(id)) {
            throw new NotFoundException("Campus nao encontrado");
        }
        campusRepository.deleteById(id);
    }
    public CampusDto toResponse(CampusEntity campusEntity){
        return new CampusDto(
                campusEntity.getNome(),
                campusEntity.getRegiao()
        );
    }

    public CampusEntity findByNome(String nome) {
        return campusRepository.findByNome(nome)
                .orElseThrow(() -> new NotFoundException("Campus '" + nome + "' nao encontrado"));
    }
}
