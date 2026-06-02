package com.reorbita.repository;

import com.reorbita.domain.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AlertRepository extends JpaRepository<Alert, UUID> {

    List<Alert> findByAcknowledgedFalseOrderByCreatedAtDesc();

    List<Alert> findBySatelliteIdOrderByCreatedAtDesc(UUID satelliteId);
}
