package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.database.model.CampusEntity;
import com.br.lukisDEV.ifscore.database.model.PartidaEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ModalidadeClassificacaoServiceTest {

    @InjectMocks
    private ModalidadeClassificacaoService classificacaoService;

    @Test
    void classificacao_ShouldReturnRanking() {
        CampusEntity c1 = CampusEntity.builder().nome("C1").build();
        CampusEntity c2 = CampusEntity.builder().nome("C2").build();
        PartidaEntity p1 = PartidaEntity.builder()
                .campus1(c1).campus2(c2)
                .placarCampus1(2).placarCampus2(1)
                .finalizada(true).chave("A")
                .build();

        Map<String, List<String>> result = classificacaoService.classificacao(List.of(p1));

        assertNotNull(result);
        assertTrue(result.containsKey("chaveA"));
        assertEquals("C1", result.get("chaveA").get(0));
    }

    @Test
    void classificacaoCompleta_ShouldReturnTable() {
        CampusEntity c1 = CampusEntity.builder().nome("C1").build();
        CampusEntity c2 = CampusEntity.builder().nome("C2").build();
        PartidaEntity p1 = PartidaEntity.builder()
                .campus1(c1).campus2(c2)
                .placarCampus1(2).placarCampus2(1)
                .finalizada(true).chave("A")
                .build();

        Map<String, List<Map<String, Object>>> result = classificacaoService.classificacaoCompleta(List.of(p1));

        assertNotNull(result);
        List<Map<String, Object>> chaveA = result.get("chaveA");
        assertEquals("C1", chaveA.get(0).get("time"));
        assertEquals(2, chaveA.get(0).get("pontos"));
    }
}
