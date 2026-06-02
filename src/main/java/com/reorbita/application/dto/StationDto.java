package com.reorbita.application.dto;

import java.util.List;
import java.util.UUID;

public record StationDto(
        UUID id,
        String name,
        double orbitAltitudeKm,
        long availableRobots,
        List<RobotDto> robots
) {
}
