package com.reorbita.application.dto;

import com.reorbita.domain.enums.RepairKind;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record DispatchRequestDto(
        @NotNull UUID satelliteId,
        @NotNull RepairKind kind,
        LocalDateTime scheduledFor,
        String notes
) {
}
