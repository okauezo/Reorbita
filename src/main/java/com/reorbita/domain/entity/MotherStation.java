package com.reorbita.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "mother_stations")
public class MotherStation {

    @Id
    private UUID id;

    @Column(length = 120)
    private String name;

    @Column(name = "orbit_altitude_km")
    private double orbitAltitudeKm;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "mother_station_id")
    private List<RepairRobot> robots = new ArrayList<>();

    protected MotherStation() {
    }

    public MotherStation(String name, double orbitAltitudeKm) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.orbitAltitudeKm = orbitAltitudeKm;
    }

    public void dockRobot(RepairRobot robot) {
        robots.add(robot);
    }

    public long availableRobotCount() {
        return robots.stream().filter(RepairRobot::isAvailable).count();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getOrbitAltitudeKm() {
        return orbitAltitudeKm;
    }

    public List<RepairRobot> getRobots() {
        return Collections.unmodifiableList(robots);
    }
}
