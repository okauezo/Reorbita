package com.reorbita.domain.entity;

import com.reorbita.domain.enums.SensorType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "sensors")
public class Sensor {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(length = 40)
    private SensorType type;

    @Column(length = 20)
    private String unit;

    @Column(nullable = false)
    private boolean active = true;

    protected Sensor() {
    }

    public Sensor(SensorType type, String unit) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.unit = unit;
    }

    public void disable() {
        this.active = false;
    }

    public UUID getId() {
        return id;
    }

    public SensorType getType() {
        return type;
    }

    public String getUnit() {
        return unit;
    }

    public boolean isActive() {
        return active;
    }
}
