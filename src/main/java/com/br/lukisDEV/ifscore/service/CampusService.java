package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.database.model.CampusEntity;
import com.br.lukisDEV.ifscore.database.repository.ICampusRepository;
import com.br.lukisDEV.ifscore.dto.CampusDto;
import com.br.lukisDEV.ifscore.exception.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
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
                .orElseThrow(() -> new NotFoundException("Campus não encontrado"));

            campus.setNome(campusDto.getNome());
            campus.setRegiao(campusDto.getRegiao());

        return campusRepository.save(campus);
    }

    @Transactional
    public void deleteCampus(UUID id) {
        if (!campusRepository.existsById(id)) {
            throw new EntityNotFoundException("Campus não encontrado");
        }
        campusRepository.deleteById(id);
    }
    public CampusDto toResponse(CampusEntity campusEntity){
        return new CampusDto(
                campusEntity.getNome(),
                campusEntity.getRegiao()
        );
    }

    @Transactional
    public void seedCampuses() {
        if (campusRepository.count() > 0) return;

        List<String> campuses = List.of(
                "Assis Chateaubriand", "Astorga", "Barracão", "Campo Largo", "Capanema",
                "Cascavel", "Colombo", "Colorado", "Coronel Vivida", "Curitiba",
                "Foz do Iguaçu", "Goioerê", "Irati", "Ivaiporã", "Jacarezinho",
                "Jaguariaíva", "Londrina", "Palmas", "Paranaguá", "Paranavaí",
                "Pinhais", "Pitanga", "Ponta Grossa", "Quedas do Iguaçu",
                "Telêmaco Borba", "Umuarama", "União da Vitória"
        );

        campuses.forEach(nome -> campusRepository.save(
                CampusEntity.builder().nome(nome).regiao("PR").build()
        ));
    }

    public void validarCampus(String nome) {
        if (!campusRepository.existsByNome(nome)) {
            throw new RuntimeException("Campus '" + nome + "' não é um campus válido do IFPR");
        }
    }
}
