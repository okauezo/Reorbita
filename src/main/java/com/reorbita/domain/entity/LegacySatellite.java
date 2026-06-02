package com.reorbita.domain.entity;

import com.reorbita.domain.vo.BatteryHealth;
import com.reorbita.domain.vo.OrbitalPosition;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("LEGACY")
public class LegacySatellite extends Satellite {

    protected LegacySatellite() {
    }

    public LegacySatellite(String name, String operatorName, String noradId, LocalDateTime launchDate,
                           OrbitalPosition position, BatteryHealth battery) {
        super(name, operatorName, noradId, launchDate, position, battery);
    }

    @Override
    public boolean supportsModularRepair() {
        return false;
    }

    @Override
    public int maintenanceComplexity() {
        return 4;
    }

    @Override
    public String designStandard() {
        return "Legacy (nao padronizado)";
    }
}
