package com.reorbita.web.controller;

import com.reorbita.application.dto.AlertDto;
import com.reorbita.application.service.AlertService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService service;

    public AlertController(AlertService service) {
        this.service = service;
    }

    @GetMapping
    public List<AlertDto> getActive() {
        return service.getActive();
    }

    @GetMapping("/satellite/{satelliteId}")
    public List<AlertDto> getForSatellite(@PathVariable UUID satelliteId) {
        return service.getForSatellite(satelliteId);
    }

    @PostMapping("/{id}/acknowledge")
    public AlertDto acknowledge(@PathVariable UUID id) {
        return service.acknowledge(id);
    }
}
