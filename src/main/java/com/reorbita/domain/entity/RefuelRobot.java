package com.reorbita.domain.entity;

import com.reorbita.domain.enums.RepairKind;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.Duration;

@Entity
@DiscriminatorValue("REFUEL")
public class RefuelRobot extends RepairRobot {

    @Column(name = "fuel_capacity_kg")
    private double fuelCapacityKg;

    protected RefuelRobot() {
    }

    public RefuelRobot(String codename, double fuelCapacityKg) {
        super(codename);
        this.fuelCapacityKg = fuelCapacityKg;
    }

    @Override
    public RepairKind specialty() {
        return RepairKind.REFUEL;
    }

    @Override
    public boolean canHandle(RepairKind kind) {
        return kind == RepairKind.REFUEL;
    }

    @Override
    public Duration estimateMissionDuration(Satellite target) {
        return Duration.ofHours(6L + target.maintenanceComplexity() * 2L);
    }

    public double getFuelCapacityKg() {
        return fuelCapacityKg;
    }
}
