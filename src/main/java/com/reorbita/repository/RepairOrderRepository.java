package com.reorbita.repository;

import com.reorbita.domain.entity.RepairOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RepairOrderRepository extends JpaRepository<RepairOrder, UUID> {

    List<RepairOrder> findAllByOrderByCreatedAtDesc();
}
