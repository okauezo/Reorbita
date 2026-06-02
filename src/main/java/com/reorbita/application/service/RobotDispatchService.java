package com.reorbita.application.service;

import com.reorbita.application.dto.DispatchRequestDto;
import com.reorbita.application.dto.RepairOrderDto;
import com.reorbita.application.dto.StationDto;

import java.util.List;
import java.util.UUID;

public interface RobotDispatchService {

    List<StationDto> getFleet();

    List<RepairOrderDto> getOrders();

    RepairOrderDto dispatch(DispatchRequestDto request);

    RepairOrderDto advance(UUID orderId);
}
