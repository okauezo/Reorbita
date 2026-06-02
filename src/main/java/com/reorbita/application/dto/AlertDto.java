package com.reorbita.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AlertDto(
        UUID id,
        UUID satelliteId,
        String severity,
        String category,
        String message,
        LocalDateTime createdAt,
        LocalDateTime predictedFailureAt,
        Integer daysUntilFailure,
        boolean acknowledged
) {
}
