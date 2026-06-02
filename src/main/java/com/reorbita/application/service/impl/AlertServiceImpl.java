package com.reorbita.application.service.impl;

import com.reorbita.application.dto.AlertDto;
import com.reorbita.application.mapper.DomainMapper;
import com.reorbita.application.service.AlertService;
import com.reorbita.domain.entity.Alert;
import com.reorbita.domain.exception.AlertNotFoundException;
import com.reorbita.repository.AlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AlertServiceImpl implements AlertService {

    private final AlertRepository repository;

    public AlertServiceImpl(AlertRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertDto> getActive() {
        LocalDateTime reference = LocalDateTime.now();
        return repository.findByAcknowledgedFalseOrderByCreatedAtDesc().stream()
                .map(alert -> DomainMapper.toDto(alert, reference))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlertDto> getForSatellite(UUID satelliteId) {
        LocalDateTime reference = LocalDateTime.now();
        return repository.findBySatelliteIdOrderByCreatedAtDesc(satelliteId).stream()
                .map(alert -> DomainMapper.toDto(alert, reference))
                .toList();
    }

    @Override
    public AlertDto acknowledge(UUID alertId) {
        Alert alert = repository.findById(alertId)
                .orElseThrow(() -> new AlertNotFoundException(alertId));
        alert.acknowledge();
        repository.save(alert);
        return DomainMapper.toDto(alert, LocalDateTime.now());
    }
}
