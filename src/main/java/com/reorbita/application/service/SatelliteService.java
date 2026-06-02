package com.reorbita.application.service;

import com.reorbita.application.dto.CreateSatelliteDto;
import com.reorbita.application.dto.SatelliteDto;

import java.util.List;
import java.util.UUID;

public interface SatelliteService {

    List<SatelliteDto> getAll();

    SatelliteDto getById(UUID id);

    SatelliteDto register(CreateSatelliteDto dto);
}
