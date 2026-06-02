package com.reorbita.domain.entity;

import com.reorbita.domain.exception.InvalidTelemetryException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "telemetry_readings")
public class TelemetryReading {

    @Id
    private UUID id;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    @Column(name = "battery_percent")
    private double batteryPercent;

    @Column(name = "fuel_percent")
    private double fuelPercent;

    @Column(name = "solar_output_watts")
    private double solarOutputWatts;

    @Column(name = "orbit_deviation_km")
    private double orbitDeviationKm;

    @Column(name = "temperature_celsius")
    private double temperatureCelsius;

    protected TelemetryReading() {
    }

    public TelemetryReading(LocalDateTime recordedAt, double batteryPercent, double fuelPercent,
                            double solarOutputWatts, double orbitDeviationKm, double temperatureCelsius) {
        if (batteryPercent < 0 || batteryPercent > 100) {
            throw new InvalidTelemetryException("A leitura de bateria deve estar entre 0 e 100 por cento.");
        }
        if (fuelPercent < 0 || fuelPercent > 100) {
            throw new InvalidTelemetryException("A leitura de combustivel deve estar entre 0 e 100 por cento.");
        }
        if (solarOutputWatts < 0) {
            throw new InvalidTelemetryException("A potencia solar nao pode ser negativa.");
        }
        this.id = UUID.randomUUID();
        this.recordedAt = recordedAt;
        this.batteryPercent = batteryPercent;
        this.fuelPercent = fuelPercent;
        this.solarOutputWatts = solarOutputWatts;
        this.orbitDeviationKm = orbitDeviationKm;
        this.temperatureCelsius = temperatureCelsius;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public double getBatteryPercent() {
        return batteryPercent;
    }

    public double getFuelPercent() {
        return fuelPercent;
    }

    public double getSolarOutputWatts() {
        return solarOutputWatts;
    }

    public double getOrbitDeviationKm() {
        return orbitDeviationKm;
    }

    public double getTemperatureCelsius() {
        return temperatureCelsius;
    }
}
