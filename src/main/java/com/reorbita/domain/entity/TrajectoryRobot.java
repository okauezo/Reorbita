package com.reorbita.domain.entity;

import com.reorbita.domain.enums.RepairKind;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.Duration;

@Entity
@DiscriminatorValue("TRAJECTORY")
public class TrajectoryRobot extends RepairRobot {

    @Column(name = "thrust_newton")
    private double thrustNewton;

    protected TrajectoryRobot() {
    }

    public TrajectoryRobot(String codename, double thrustNewton) {
        super(codename);
        this.thrustNewton = thrustNewton;
    }

    @Override
    public RepairKind specialty() {
        return RepairKind.TRAJECTORY_CORRECTION;
    }

    @Override
    public boolean canHandle(RepairKind kind) {
        return kind == RepairKind.TRAJECTORY_CORRECTION;
    }

    @Override
    public Duration estimateMissionDuration(Satellite target) {
        return Duration.ofHours(4L + target.maintenanceComplexity());
    }

    public double getThrustNewton() {
        return thrustNewton;
    }
}
