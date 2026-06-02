package com.reorbita.application.dto;

import java.util.UUID;

public record RobotDto(
        UUID id,
        String codename,
        String specialty,
        boolean available
) {
}
