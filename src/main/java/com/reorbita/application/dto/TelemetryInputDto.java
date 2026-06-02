package com.reorbita.application.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

import java.time.LocalDateTime;

public record TelemetryInputDto(
        LocalDateTime recordedAt,
        @DecimalMin("0") @DecimalMax("100") double batteryPercent,
        @DecimalMin("0") @DecimalMax("100") double fuelPercent,
        @DecimalMin("0") @DecimalMax("100000") double solarOutputWatts,
        @DecimalMin("0") @DecimalMax("10000") double orbitDeviationKm,
        double temperatureCelsius
) {
}
