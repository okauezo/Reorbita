package com.reorbita.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record TelemetryReadingDto(
        UUID id,
        UUID satelliteId,
        LocalDateTime recordedAt,
        double batteryPercent,
        double fuelPercent,
        double solarOutputWatts,
        double orbitDeviationKm,
        double temperatureCelsius
) {
}
