package com.br.lukisDEV.ifscore.controller;

import com.br.lukisDEV.ifscore.service.ModalidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/classificacao")
@RequiredArgsConstructor
public class ModalidadeClassificacaoController {

    private final ModalidadeService modalidadeService;

    @GetMapping("/{id}")
    public Map<String, List<String>> getClassificacao(@PathVariable UUID id) {
        return modalidadeService.classificacao(id);
    }

    @GetMapping("/{id}/completa")
    public Map<String, List<Map<String, Object>>> getClassificacaoCompleta(@PathVariable UUID id) {
        return modalidadeService.classificacaoCompleta(id);
    }
}
