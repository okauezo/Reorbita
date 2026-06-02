package com.reorbita.application.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateSatelliteDto(
        @NotBlank String name,
        @NotBlank String operatorName,
        @NotBlank String noradId,
        boolean orbitReady,
        String dockingInterfaceVersion,
        @NotNull LocalDateTime launchDate,
        @DecimalMin("100") @DecimalMax("40000") double altitudeKm,
        @DecimalMin("0") @DecimalMax("180") double inclinationDeg,
        @DecimalMin("0.1") @DecimalMax("12") double velocityKmS,
        @DecimalMin("0") @DecimalMax("100") double batteryCapacityPercent,
        @Min(0) @Max(100000) int chargeCycles
) {
}
