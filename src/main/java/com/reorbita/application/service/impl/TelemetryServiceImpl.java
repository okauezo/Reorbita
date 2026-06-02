package com.reorbita.application.service.impl;

import com.reorbita.application.dto.TelemetryInputDto;
import com.reorbita.application.dto.TelemetryReadingDto;
import com.reorbita.application.mapper.DomainMapper;
import com.reorbita.application.service.TelemetryService;
import com.reorbita.domain.entity.Satellite;
import com.reorbita.domain.entity.TelemetryReading;
import com.reorbita.domain.exception.SatelliteNotFoundException;
import com.reorbita.repository.SatelliteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TelemetryServiceImpl implements TelemetryService {

    private final SatelliteRepository repository;

    public TelemetryServiceImpl(SatelliteRepository repository) {
        this.repository = repository;
    }

    @Override
    public TelemetryReadingDto ingest(UUID satelliteId, TelemetryInputDto dto) {
        Satellite satellite = repository.findById(satelliteId)
                .orElseThrow(() -> new SatelliteNotFoundException(satelliteId));

        LocalDateTime recordedAt = dto.recordedAt() != null ? dto.recordedAt() : LocalDateTime.now();
        TelemetryReading reading = new TelemetryReading(
                recordedAt,
                dto.batteryPercent(),
                dto.fuelPercent(),
                dto.solarOutputWatts(),
                dto.orbitDeviationKm(),
                dto.temperatureCelsius());

        satellite.pushTelemetry(reading);
        repository.save(satellite);

        return DomainMapper.toDto(reading, satellite.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TelemetryReadingDto> getHistory(UUID satelliteId, int days) {
        Satellite satellite = repository.findById(satelliteId)
                .orElseThrow(() -> new SatelliteNotFoundException(satelliteId));

        int window = days <= 0 ? 30 : days;
        LocalDateTime since = LocalDateTime.now().minusDays(window);

        return satellite.getTelemetry().stream()
                .filter(reading -> !reading.getRecordedAt().isBefore(since))
                .sorted(Comparator.comparing(TelemetryReading::getRecordedAt))
                .map(reading -> DomainMapper.toDto(reading, satellite.getId()))
                .toList();
    }
}
