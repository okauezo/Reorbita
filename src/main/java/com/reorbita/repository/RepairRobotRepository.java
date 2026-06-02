package com.reorbita.repository;

import com.reorbita.domain.entity.RepairRobot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RepairRobotRepository extends JpaRepository<RepairRobot, UUID> {
}
