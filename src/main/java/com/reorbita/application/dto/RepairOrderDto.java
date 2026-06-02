package com.reorbita.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record RepairOrderDto(
        UUID id,
        UUID satelliteId,
        UUID robotId,
        String robotCodename,
        String kind,
        String status,
        LocalDateTime createdAt,
        LocalDateTime scheduledFor,
        LocalDateTime completedAt,
        double estimatedMissionHours,
        String notes
) {
}
