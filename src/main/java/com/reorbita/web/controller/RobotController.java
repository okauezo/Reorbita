package com.reorbita.web.controller;

import com.reorbita.application.dto.DispatchRequestDto;
import com.reorbita.application.dto.RepairOrderDto;
import com.reorbita.application.dto.StationDto;
import com.reorbita.application.service.RobotDispatchService;
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
@RequestMapping("/api/robots")
public class RobotController {

    private final RobotDispatchService service;

    public RobotController(RobotDispatchService service) {
        this.service = service;
    }

    @GetMapping("/fleet")
    public List<StationDto> getFleet() {
        return service.getFleet();
    }

    @GetMapping("/orders")
    public List<RepairOrderDto> getOrders() {
        return service.getOrders();
    }

    @PostMapping("/dispatch")
    public ResponseEntity<RepairOrderDto> dispatch(@Valid @RequestBody DispatchRequestDto request) {
        RepairOrderDto order = service.dispatch(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PostMapping("/orders/{orderId}/advance")
    public RepairOrderDto advance(@PathVariable UUID orderId) {
        return service.advance(orderId);
    }
}
