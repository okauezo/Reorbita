package com.reorbita.web.controller;

import com.reorbita.application.dto.CreateSatelliteDto;
import com.reorbita.application.dto.SatelliteDto;
import com.reorbita.application.service.SatelliteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/satellites")
public class SatelliteController {

    private final SatelliteService service;

    public SatelliteController(SatelliteService service) {
        this.service = service;
    }

    @GetMapping
    public List<SatelliteDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public SatelliteDto getById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PostMapping
    public ResponseEntity<SatelliteDto> register(@Valid @RequestBody CreateSatelliteDto dto) {
        SatelliteDto created = service.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
