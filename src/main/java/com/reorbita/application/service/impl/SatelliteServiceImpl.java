package com.reorbita.application.service.impl;

import com.reorbita.application.dto.CreateSatelliteDto;
import com.reorbita.application.dto.SatelliteDto;
import com.reorbita.application.mapper.DomainMapper;
import com.reorbita.application.service.SatelliteService;
import com.reorbita.domain.entity.LegacySatellite;
import com.reorbita.domain.entity.OrbitReadySatellite;
import com.reorbita.domain.entity.Satellite;
import com.reorbita.domain.exception.DuplicateSatelliteException;
import com.reorbita.domain.exception.SatelliteNotFoundException;
import com.reorbita.domain.vo.BatteryHealth;
import com.reorbita.domain.vo.OrbitalPosition;
import com.reorbita.repository.SatelliteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SatelliteServiceImpl implements SatelliteService {

    private final SatelliteRepository repository;

    public SatelliteServiceImpl(SatelliteRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SatelliteDto> getAll() {
        LocalDateTime reference = LocalDateTime.now();
        return repository.findAll().stream()
                .map(satellite -> DomainMapper.toDto(satellite, reference))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SatelliteDto getById(UUID id) {
        Satellite satellite = repository.findById(id)
                .orElseThrow(() -> new SatelliteNotFoundException(id));
        return DomainMapper.toDto(satellite, LocalDateTime.now());
    }

    @Override
    public SatelliteDto register(CreateSatelliteDto dto) {
        if (repository.existsByNoradId(dto.noradId())) {
            throw new DuplicateSatelliteException(dto.noradId());
        }

        OrbitalPosition position = new OrbitalPosition(dto.altitudeKm(), dto.inclinationDeg(), dto.velocityKmS());
        BatteryHealth battery = new BatteryHealth(dto.batteryCapacityPercent(), dto.chargeCycles(), LocalDateTime.now());

        Satellite satellite = dto.orbitReady()
                ? new OrbitReadySatellite(dto.name(), dto.operatorName(), dto.noradId(), dto.launchDate(), position, battery, dto.dockingInterfaceVersion())
                : new LegacySatellite(dto.name(), dto.operatorName(), dto.noradId(), dto.launchDate(), position, battery);

        Satellite saved = repository.save(satellite);
        return DomainMapper.toDto(saved, LocalDateTime.now());
    }
}
