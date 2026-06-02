package com.reorbita.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PredictionDto(
        UUID satelliteId,
        String satelliteName,
        int sampleCount,
        double batteryDropPerDay,
        double currentBatteryPercent,
        LocalDateTime predictedFailureAt,
        Integer daysUntilFailure,
        String recommendation,
        boolean alertRaised
) {
}
