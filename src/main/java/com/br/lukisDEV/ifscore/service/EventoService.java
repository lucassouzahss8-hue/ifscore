package com.br.lukisDEV.ifscore.service;

import com.br.lukisDEV.ifscore.database.model.EventoEntity;
import com.br.lukisDEV.ifscore.database.repository.IEventoRepository;
import com.br.lukisDEV.ifscore.dto.EventoDto;
import com.br.lukisDEV.ifscore.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final IEventoRepository eventoRepository;
    private final CampusService campusService;

    public List<EventoEntity> findAll() {
        return eventoRepository.findAll();
    }

    public EventoEntity findById(UUID id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Evento nao encontrado"));
    }

    @Transactional
    public EventoEntity criarEvento(EventoDto dto) {

        campusService.validarCampus(dto.getLocal());

        EventoEntity evento = new EventoEntity();

        evento.setNome(dto.getNome());
        evento.setLocal(dto.getLocal());
        evento.setData(dto.getData());

        return eventoRepository.save(evento);
    }

    @Transactional
    public EventoEntity updateEvento(UUID id, EventoDto dto) {

        campusService.validarCampus(dto.getLocal());

        EventoEntity evento = findById(id);

        evento.setNome(dto.getNome());
        evento.setLocal(dto.getLocal());
        evento.setData(dto.getData());

        return eventoRepository.save(evento);
    }

    @Transactional
    public void deleteEvento(UUID id) {

        if (!eventoRepository.existsById(id)) {
            throw new NotFoundException("Evento nao encontrado");
        }

        eventoRepository.deleteById(id);
    }
}
