package com.reorbita.domain.entity;

import com.reorbita.domain.enums.RepairKind;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.Duration;

@Entity
@DiscriminatorValue("DEBRIS_CAPTURE")
public class DebrisCaptureRobot extends RepairRobot {

    @Column(name = "net_span_meters")
    private double netSpanMeters;

    protected DebrisCaptureRobot() {
    }

    public DebrisCaptureRobot(String codename, double netSpanMeters) {
        super(codename);
        this.netSpanMeters = netSpanMeters;
    }

    @Override
    public RepairKind specialty() {
        return RepairKind.DEBRIS_CAPTURE;
    }

    @Override
    public boolean canHandle(RepairKind kind) {
        return kind == RepairKind.DEBRIS_CAPTURE;
    }

    @Override
    public Duration estimateMissionDuration(Satellite target) {
        return Duration.ofHours(5L + target.maintenanceComplexity() * 2L);
    }

    public double getNetSpanMeters() {
        return netSpanMeters;
    }
}
