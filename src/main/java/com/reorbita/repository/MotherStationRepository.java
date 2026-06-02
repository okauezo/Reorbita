package com.reorbita.repository;

import com.reorbita.domain.entity.MotherStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MotherStationRepository extends JpaRepository<MotherStation, UUID> {
}
