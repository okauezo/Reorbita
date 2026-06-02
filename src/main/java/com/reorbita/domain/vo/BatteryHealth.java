package com.reorbita.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class BatteryHealth {

    @Column(name = "battery_capacity_percent")
    private double capacityPercent;

    @Column(name = "battery_charge_cycles")
    private int chargeCycles;

    @Column(name = "battery_measured_at")
    private LocalDateTime measuredAt;

    protected BatteryHealth() {
    }

    public BatteryHealth(double capacityPercent, int chargeCycles, LocalDateTime measuredAt) {
        if (capacityPercent < 0 || capacityPercent > 100) {
            throw new IllegalArgumentException("A capacidade deve estar entre 0 e 100 por cento.");
        }
        if (chargeCycles < 0) {
            throw new IllegalArgumentException("O numero de ciclos nao pode ser negativo.");
        }
        this.capacityPercent = capacityPercent;
        this.chargeCycles = chargeCycles;
        this.measuredAt = measuredAt;
    }

    public double getCapacityPercent() {
        return capacityPercent;
    }

    public int getChargeCycles() {
        return chargeCycles;
    }

    public LocalDateTime getMeasuredAt() {
        return measuredAt;
    }

    public boolean isHealthy() {
        return capacityPercent >= 60;
    }

    public boolean requiresAttention() {
        return capacityPercent < 40;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BatteryHealth other)) {
            return false;
        }
        return Double.compare(capacityPercent, other.capacityPercent) == 0
                && chargeCycles == other.chargeCycles
                && Objects.equals(measuredAt, other.measuredAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(capacityPercent, chargeCycles, measuredAt);
    }
}
