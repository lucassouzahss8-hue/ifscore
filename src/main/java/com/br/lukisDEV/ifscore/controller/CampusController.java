package com.br.lukisDEV.ifscore.controller;

import com.br.lukisDEV.ifscore.dto.CampusDto;
import com.br.lukisDEV.ifscore.dto.CampusResponseDto;
import com.br.lukisDEV.ifscore.service.CampusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/campus")
@RequiredArgsConstructor
@Validated
public class CampusController {
    private final CampusService campusService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CampusResponseDto> findAll(){
        return campusService.findAll().stream().map(CampusResponseDto::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveCampus(@Valid @RequestBody CampusDto campusDto){
        campusService.save(campusDto);
    }
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CampusResponseDto updateCampus(@PathVariable UUID id, @Valid @RequestBody CampusDto campusDto){
        return CampusResponseDto.from(campusService.updateCampus(id, campusDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCampus(@PathVariable UUID id) {
        campusService.deleteCampus(id);
    }
}
