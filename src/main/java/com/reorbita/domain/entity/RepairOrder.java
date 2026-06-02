package com.reorbita.domain.entity;

import com.reorbita.domain.enums.RepairKind;
import com.reorbita.domain.enums.RepairStatus;
import com.reorbita.domain.exception.InvalidRepairOrderException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "repair_orders")
public class RepairOrder {

    @Id
    private UUID id;

    @Column(name = "satellite_id", nullable = false)
    private UUID satelliteId;

    @Column(name = "robot_id", nullable = false)
    private UUID robotId;

    @Enumerated(EnumType.STRING)
    @Column(length = 40)
    private RepairKind kind;

    @Enumerated(EnumType.STRING)
    @Column(length = 40)
    private RepairStatus status = RepairStatus.SCHEDULED;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "scheduled_for", nullable = false)
    private LocalDateTime scheduledFor;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(length = 400)
    private String notes;

    protected RepairOrder() {
    }

    public RepairOrder(UUID satelliteId, UUID robotId, RepairKind kind, LocalDateTime createdAt,
                       LocalDateTime scheduledFor, String notes) {
        if (scheduledFor.isBefore(createdAt)) {
            throw new InvalidRepairOrderException("A data de execucao nao pode ser anterior a criacao da ordem.");
        }
        this.id = UUID.randomUUID();
        this.satelliteId = satelliteId;
        this.robotId = robotId;
        this.kind = kind;
        this.createdAt = createdAt;
        this.scheduledFor = scheduledFor;
        this.notes = notes;
    }

    public void dispatch() {
        ensureStatus(RepairStatus.SCHEDULED);
        this.status = RepairStatus.DISPATCHED;
    }

    public void start() {
        ensureStatus(RepairStatus.DISPATCHED);
        this.status = RepairStatus.IN_PROGRESS;
    }

    public void complete(LocalDateTime when) {
        ensureStatus(RepairStatus.IN_PROGRESS);
        this.status = RepairStatus.COMPLETED;
        this.completedAt = when;
    }

    public void abort(String reason) {
        if (status == RepairStatus.COMPLETED) {
            throw new InvalidRepairOrderException("Uma ordem concluida nao pode ser abortada.");
        }
        this.status = RepairStatus.ABORTED;
        if (reason != null && !reason.isBlank()) {
            this.notes = reason;
        }
    }

    private void ensureStatus(RepairStatus expected) {
        if (status != expected) {
            throw new InvalidRepairOrderException("Transicao invalida: a ordem esta " + status + " e nao " + expected + ".");
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getSatelliteId() {
        return satelliteId;
    }

    public UUID getRobotId() {
        return robotId;
    }

    public RepairKind getKind() {
        return kind;
    }

    public RepairStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getScheduledFor() {
        return scheduledFor;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public String getNotes() {
        return notes;
    }
}
