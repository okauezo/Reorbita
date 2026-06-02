package com.reorbita.web.controller;

import com.reorbita.application.dto.TelemetryInputDto;
import com.reorbita.application.dto.TelemetryReadingDto;
import com.reorbita.application.service.TelemetryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/satellites/{satelliteId}/telemetry")
public class TelemetryController {

    private final TelemetryService service;

    public TelemetryController(TelemetryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TelemetryReadingDto> ingest(@PathVariable UUID satelliteId,
                                                      @Valid @RequestBody TelemetryInputDto dto) {
        TelemetryReadingDto reading = service.ingest(satelliteId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reading);
    }

    @GetMapping
    public List<TelemetryReadingDto> getHistory(@PathVariable UUID satelliteId,
                                                @RequestParam(defaultValue = "30") int days) {
        return service.getHistory(satelliteId, days);
    }
}
