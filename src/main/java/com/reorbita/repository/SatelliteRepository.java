package com.reorbita.repository;

import com.reorbita.domain.entity.Satellite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SatelliteRepository extends JpaRepository<Satellite, UUID> {

    boolean existsByNoradId(String noradId);
}
