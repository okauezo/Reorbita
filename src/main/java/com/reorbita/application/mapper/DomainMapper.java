package com.reorbita.application.mapper;

import com.reorbita.application.dto.AlertDto;
import com.reorbita.application.dto.RepairOrderDto;
import com.reorbita.application.dto.RobotDto;
import com.reorbita.application.dto.SatelliteDto;
import com.reorbita.application.dto.StationDto;
import com.reorbita.application.dto.TelemetryReadingDto;
import com.reorbita.domain.entity.Alert;
import com.reorbita.domain.entity.MotherStation;
import com.reorbita.domain.entity.RepairOrder;
import com.reorbita.domain.entity.RepairRobot;
import com.reorbita.domain.entity.Satellite;
import com.reorbita.domain.entity.TelemetryReading;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class DomainMapper {

    private DomainMapper() {
    }

    public static SatelliteDto toDto(Satellite satellite, LocalDateTime reference) {
        return new SatelliteDto(
                satellite.getId(),
                satellite.getName(),
                satellite.getOperatorName(),
                satellite.getNoradId(),
                satellite.getStatus().name(),
                satellite.designStandard(),
                satellite.supportsModularRepair(),
                satellite.maintenanceComplexity(),
                satellite.getLaunchDate(),
                satellite.ageInYears(reference),
                satellite.getPosition().getAltitudeKm(),
                satellite.getPosition().getInclinationDeg(),
                satellite.getBattery().getCapacityPercent(),
                satellite.getSensors().size()
        );
    }

    public static TelemetryReadingDto toDto(TelemetryReading reading, UUID satelliteId) {
        return new TelemetryReadingDto(
                reading.getId(),
                satelliteId,
                reading.getRecordedAt(),
                reading.getBatteryPercent(),
                reading.getFuelPercent(),
                reading.getSolarOutputWatts(),
                reading.getOrbitDeviationKm(),
                reading.getTemperatureCelsius()
        );
    }

    public static AlertDto toDto(Alert alert, LocalDateTime reference) {
        return new AlertDto(
                alert.getId(),
                alert.getSatelliteId(),
                alert.getSeverity().name(),
                alert.getCategory(),
                alert.getMessage(),
                alert.getCreatedAt(),
                alert.getPredictedFailureAt(),
                alert.daysUntilFailure(reference),
                alert.isAcknowledged()
        );
    }

    public static RobotDto toDto(RepairRobot robot) {
        return new RobotDto(
                robot.getId(),
                robot.getCodename(),
                robot.specialty().name(),
                robot.isAvailable()
        );
    }

    public static StationDto toDto(MotherStation station) {
        List<RobotDto> robots = station.getRobots().stream()
                .map(DomainMapper::toDto)
                .toList();
        return new StationDto(
                station.getId(),
                station.getName(),
                station.getOrbitAltitudeKm(),
                station.availableRobotCount(),
                robots
        );
    }

    public static RepairOrderDto toDto(RepairOrder order, RepairRobot robot, Satellite satellite) {
        double hours = Math.round(robot.estimateMissionDuration(satellite).toMinutes() / 60.0 * 10.0) / 10.0;
        return new RepairOrderDto(
                order.getId(),
                order.getSatelliteId(),
                order.getRobotId(),
                robot.getCodename(),
                order.getKind().name(),
                order.getStatus().name(),
                order.getCreatedAt(),
                order.getScheduledFor(),
                order.getCompletedAt(),
                hours,
                order.getNotes()
        );
    }
}
