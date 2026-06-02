package com.reorbita.application.service.impl;

import com.reorbita.application.dto.PredictionDto;
import com.reorbita.application.service.PredictiveMaintenanceService;
import com.reorbita.domain.entity.Alert;
import com.reorbita.domain.entity.Satellite;
import com.reorbita.domain.entity.TelemetryReading;
import com.reorbita.domain.enums.AlertSeverity;
import com.reorbita.domain.exception.SatelliteNotFoundException;
import com.reorbita.repository.AlertRepository;
import com.reorbita.repository.SatelliteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PredictiveMaintenanceServiceImpl implements PredictiveMaintenanceService {

    private static final double FAILURE_THRESHOLD_PERCENT = 20.0;
    private static final int ALERT_HORIZON_DAYS = 150;

    private final SatelliteRepository satelliteRepository;
    private final AlertRepository alertRepository;

    public PredictiveMaintenanceServiceImpl(SatelliteRepository satelliteRepository, AlertRepository alertRepository) {
        this.satelliteRepository = satelliteRepository;
        this.alertRepository = alertRepository;
    }

    @Override
    public PredictionDto analyze(UUID satelliteId) {
        Satellite satellite = satelliteRepository.findById(satelliteId)
                .orElseThrow(() -> new SatelliteNotFoundException(satelliteId));

        List<TelemetryReading> readings = satellite.getTelemetry().stream()
                .sorted(Comparator.comparing(TelemetryReading::getRecordedAt))
                .toList();

        if (readings.size() < 2) {
            return new PredictionDto(satellite.getId(), satellite.getName(), readings.size(),
                    0.0, satellite.getBattery().getCapacityPercent(), null, null,
                    "Amostras insuficientes de telemetria para projecao confiavel.", false);
        }

        double dropPerDay = estimateBatteryDropPerDay(readings);
        double current = readings.get(readings.size() - 1).getBatteryPercent();
        LocalDateTime now = LocalDateTime.now();

        if (dropPerDay <= 0.01) {
            return new PredictionDto(satellite.getId(), satellite.getName(), readings.size(),
                    round(dropPerDay, 4), round(current, 2), null, null,
                    "Bateria estavel. Manutencao preditiva nao requerida no horizonte atual.", false);
        }

        double daysUntilFailure = (current - FAILURE_THRESHOLD_PERCENT) / dropPerDay;
        LocalDateTime predictedFailureAt = now.plusMinutes((long) Math.round(daysUntilFailure * 24 * 60));
        int roundedDays = (int) Math.ceil(daysUntilFailure);

        boolean alertRaised = false;
        if (daysUntilFailure <= ALERT_HORIZON_DAYS) {
            AlertSeverity severity = classifySeverity(daysUntilFailure);
            Alert alert = new Alert(satellite.getId(), severity, "Bateria",
                    "Projecao indica queda abaixo de " + (int) FAILURE_THRESHOLD_PERCENT
                            + "% em aproximadamente " + roundedDays + " dias.",
                    now, predictedFailureAt);
            alertRepository.save(alert);
            alertRaised = true;
        }

        return new PredictionDto(satellite.getId(), satellite.getName(), readings.size(),
                round(dropPerDay, 4), round(current, 2), predictedFailureAt, roundedDays,
                buildRecommendation(roundedDays), alertRaised);
    }

    private double estimateBatteryDropPerDay(List<TelemetryReading> readings) {
        LocalDateTime origin = readings.get(0).getRecordedAt();
        int n = readings.size();
        double sumX = 0, sumY = 0, sumXy = 0, sumXx = 0;

        for (TelemetryReading reading : readings) {
            double x = Duration.between(origin, reading.getRecordedAt()).toMinutes() / (60.0 * 24.0);
            double y = reading.getBatteryPercent();
            sumX += x;
            sumY += y;
            sumXy += x * y;
            sumXx += x * x;
        }

        double denominator = n * sumXx - sumX * sumX;
        if (Math.abs(denominator) < 1e-9) {
            return 0.0;
        }
        double slope = (n * sumXy - sumX * sumY) / denominator;
        return -slope;
    }

    private AlertSeverity classifySeverity(double daysUntilFailure) {
        if (daysUntilFailure <= 30) {
            return AlertSeverity.CRITICAL;
        }
        if (daysUntilFailure <= 75) {
            return AlertSeverity.HIGH;
        }
        return AlertSeverity.WARNING;
    }

    private String buildRecommendation(int days) {
        if (days <= 30) {
            return "Agendar troca de modulo de bateria imediatamente.";
        }
        if (days <= 75) {
            return "Planejar intervencao da frota nas proximas semanas.";
        }
        if (days <= ALERT_HORIZON_DAYS) {
            return "Monitorar de perto e reservar janela de manutencao.";
        }
        return "Acompanhamento de rotina suficiente.";
    }

    private double round(double value, int decimals) {
        double factor = Math.pow(10, decimals);
        return Math.round(value * factor) / factor;
    }
}
