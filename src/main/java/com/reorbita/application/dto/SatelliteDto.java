package com.reorbita.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SatelliteDto(
        UUID id,
        String name,
        String operatorName,
        String noradId,
        String status,
        String designStandard,
        boolean supportsModularRepair,
        int maintenanceComplexity,
        LocalDateTime launchDate,
        int ageInYears,
        double altitudeKm,
        double inclinationDeg,
        double batteryCapacityPercent,
        int sensorCount
) {
}
