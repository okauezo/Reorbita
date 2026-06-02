package com.reorbita.application.service;

import com.reorbita.application.dto.TelemetryInputDto;
import com.reorbita.application.dto.TelemetryReadingDto;

import java.util.List;
import java.util.UUID;

public interface TelemetryService {

    TelemetryReadingDto ingest(UUID satelliteId, TelemetryInputDto dto);

    List<TelemetryReadingDto> getHistory(UUID satelliteId, int days);
}
