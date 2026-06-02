package com.reorbita.domain.entity;

import com.reorbita.domain.enums.RepairKind;
import com.reorbita.domain.exception.RobotUnavailableException;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

import java.time.Duration;
import java.util.UUID;

@Entity
@Table(name = "robots")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "robot_type")
public abstract class RepairRobot {

    @Id
    private UUID id;

    @Column(length = 80)
    private String codename;

    @Column(nullable = false)
    private boolean available = true;

    protected RepairRobot() {
    }

    protected RepairRobot(String codename) {
        this.id = UUID.randomUUID();
        this.codename = codename;
    }

    public abstract RepairKind specialty();

    public abstract boolean canHandle(RepairKind kind);

    public abstract Duration estimateMissionDuration(Satellite target);

    public void dispatch() {
        if (!available) {
            throw new RobotUnavailableException("O robo " + codename + " ja esta em missao.");
        }
        this.available = false;
    }

    public void returnToStation() {
        this.available = true;
    }

    public UUID getId() {
        return id;
    }

    public String getCodename() {
        return codename;
    }

    public boolean isAvailable() {
        return available;
    }
}
