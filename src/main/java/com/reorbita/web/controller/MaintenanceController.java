package com.reorbita.web.controller;

import com.reorbita.application.dto.PredictionDto;
import com.reorbita.application.service.PredictiveMaintenanceService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/satellites/{satelliteId}/maintenance")
public class MaintenanceController {

    private final PredictiveMaintenanceService service;

    public MaintenanceController(PredictiveMaintenanceService service) {
        this.service = service;
    }

    @PostMapping("/analyze")
    public PredictionDto analyze(@PathVariable UUID satelliteId) {
        return service.analyze(satelliteId);
    }
}
