package com.reorbita.domain.entity;

import com.reorbita.domain.enums.AlertSeverity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "alerts")
public class Alert {

    @Id
    private UUID id;

    @Column(name = "satellite_id", nullable = false)
    private UUID satelliteId;

    @Enumerated(EnumType.STRING)
    @Column(length = 40)
    private AlertSeverity severity;

    @Column(length = 80)
    private String category;

    @Column(length = 400)
    private String message;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "predicted_failure_at")
    private LocalDateTime predictedFailureAt;

    @Column(nullable = false)
    private boolean acknowledged;

    protected Alert() {
    }

    public Alert(UUID satelliteId, AlertSeverity severity, String category, String message,
                 LocalDateTime createdAt, LocalDateTime predictedFailureAt) {
        this.id = UUID.randomUUID();
        this.satelliteId = satelliteId;
        this.severity = severity;
        this.category = category;
        this.message = message;
        this.createdAt = createdAt;
        this.predictedFailureAt = predictedFailureAt;
    }

    public void acknowledge() {
        this.acknowledged = true;
    }

    public Integer daysUntilFailure(LocalDateTime reference) {
        if (predictedFailureAt == null) {
            return null;
        }
        long minutes = Duration.between(reference, predictedFailureAt).toMinutes();
        return (int) Math.ceil(minutes / (60.0 * 24.0));
    }

    public UUID getId() {
        return id;
    }

    public UUID getSatelliteId() {
        return satelliteId;
    }

    public AlertSeverity getSeverity() {
        return severity;
    }

    public String getCategory() {
        return category;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getPredictedFailureAt() {
        return predictedFailureAt;
    }

    public boolean isAcknowledged() {
        return acknowledged;
    }
}
