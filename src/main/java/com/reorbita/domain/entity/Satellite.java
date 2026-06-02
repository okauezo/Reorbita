package com.reorbita.domain.entity;

import com.reorbita.domain.enums.SatelliteStatus;
import com.reorbita.domain.exception.InvalidSatelliteException;
import com.reorbita.domain.vo.BatteryHealth;
import com.reorbita.domain.vo.OrbitalPosition;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "satellites")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "satellite_type")
public abstract class Satellite {

    @Id
    private UUID id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "operator_name", length = 120)
    private String operatorName;

    @Column(name = "norad_id", length = 40)
    private String noradId;

    @Column(name = "launch_date", nullable = false)
    private LocalDateTime launchDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 40)
    private SatelliteStatus status = SatelliteStatus.OPERATIONAL;

    @Embedded
    private OrbitalPosition position;

    @Embedded
    private BatteryHealth battery;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "satellite_id")
    private List<Sensor> sensors = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "satellite_id")
    private List<TelemetryReading> telemetry = new ArrayList<>();

    protected Satellite() {
    }

    protected Satellite(String name, String operatorName, String noradId, LocalDateTime launchDate,
                        OrbitalPosition position, BatteryHealth battery) {
        if (name == null || name.isBlank()) {
            throw new InvalidSatelliteException("O nome do satelite e obrigatorio.");
        }
        if (launchDate != null && launchDate.isAfter(LocalDateTime.now())) {
            throw new InvalidSatelliteException("A data de lancamento nao pode estar no futuro.");
        }
        if (position == null) {
            throw new InvalidSatelliteException("A posicao orbital e obrigatoria.");
        }
        if (battery == null) {
            throw new InvalidSatelliteException("O estado da bateria e obrigatorio.");
        }
        this.id = UUID.randomUUID();
        this.name = name;
        this.operatorName = operatorName;
        this.noradId = noradId;
        this.launchDate = launchDate;
        this.position = position;
        this.battery = battery;
    }

    public abstract boolean supportsModularRepair();

    public abstract int maintenanceComplexity();

    public abstract String designStandard();

    public int ageInYears(LocalDateTime reference) {
        LocalDate from = launchDate.toLocalDate();
        LocalDate to = reference.toLocalDate();
        int years = Period.between(from, to).getYears();
        return Math.max(years, 0);
    }

    public Duration timeInOrbit(LocalDateTime reference) {
        return Duration.between(launchDate, reference);
    }

    public void registerSensor(Sensor sensor) {
        sensors.add(sensor);
    }

    public void pushTelemetry(TelemetryReading reading) {
        telemetry.add(reading);
        this.battery = new BatteryHealth(reading.getBatteryPercent(), battery.getChargeCycles() + 1, reading.getRecordedAt());
        reevaluateStatus();
    }

    public void markUnderMaintenance() {
        if (status == SatelliteStatus.DECOMMISSIONED) {
            throw new InvalidSatelliteException("Um satelite desativado nao pode entrar em manutencao.");
        }
        this.status = SatelliteStatus.UNDER_MAINTENANCE;
    }

    public void restoreToOperational() {
        this.status = SatelliteStatus.OPERATIONAL;
    }

    private void reevaluateStatus() {
        if (status == SatelliteStatus.UNDER_MAINTENANCE || status == SatelliteStatus.DECOMMISSIONED) {
            return;
        }
        double capacity = battery.getCapacityPercent();
        if (capacity < 25) {
            this.status = SatelliteStatus.CRITICAL;
        } else if (capacity < 50) {
            this.status = SatelliteStatus.DEGRADED;
        } else {
            this.status = SatelliteStatus.OPERATIONAL;
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public String getNoradId() {
        return noradId;
    }

    public LocalDateTime getLaunchDate() {
        return launchDate;
    }

    public SatelliteStatus getStatus() {
        return status;
    }

    public OrbitalPosition getPosition() {
        return position;
    }

    public BatteryHealth getBattery() {
        return battery;
    }

    public List<Sensor> getSensors() {
        return Collections.unmodifiableList(sensors);
    }

    public List<TelemetryReading> getTelemetry() {
        return Collections.unmodifiableList(telemetry);
    }
}
