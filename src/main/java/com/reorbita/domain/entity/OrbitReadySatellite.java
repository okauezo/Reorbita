package com.reorbita.domain.entity;

import com.reorbita.domain.vo.BatteryHealth;
import com.reorbita.domain.vo.OrbitalPosition;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("ORBIT_READY")
public class OrbitReadySatellite extends Satellite {

    @Column(name = "docking_interface_version", length = 40)
    private String dockingInterfaceVersion = "ORB-C 1.0";

    protected OrbitReadySatellite() {
    }

    public OrbitReadySatellite(String name, String operatorName, String noradId, LocalDateTime launchDate,
                               OrbitalPosition position, BatteryHealth battery, String dockingInterfaceVersion) {
        super(name, operatorName, noradId, launchDate, position, battery);
        if (dockingInterfaceVersion != null && !dockingInterfaceVersion.isBlank()) {
            this.dockingInterfaceVersion = dockingInterfaceVersion;
        }
    }

    @Override
    public boolean supportsModularRepair() {
        return true;
    }

    @Override
    public int maintenanceComplexity() {
        return 1;
    }

    @Override
    public String designStandard() {
        return "Orbit-Ready " + dockingInterfaceVersion;
    }

    public String getDockingInterfaceVersion() {
        return dockingInterfaceVersion;
    }
}
