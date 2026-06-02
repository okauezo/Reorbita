package com.reorbita.application.service.impl;

import com.reorbita.application.dto.DispatchRequestDto;
import com.reorbita.application.dto.RepairOrderDto;
import com.reorbita.application.dto.StationDto;
import com.reorbita.application.mapper.DomainMapper;
import com.reorbita.application.service.RobotDispatchService;
import com.reorbita.domain.entity.RepairOrder;
import com.reorbita.domain.entity.RepairRobot;
import com.reorbita.domain.entity.Satellite;
import com.reorbita.domain.exception.InvalidRepairOrderException;
import com.reorbita.domain.exception.RepairOrderNotFoundException;
import com.reorbita.domain.exception.RobotUnavailableException;
import com.reorbita.domain.exception.SatelliteNotFoundException;
import com.reorbita.repository.MotherStationRepository;
import com.reorbita.repository.RepairOrderRepository;
import com.reorbita.repository.RepairRobotRepository;
import com.reorbita.repository.SatelliteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class RobotDispatchServiceImpl implements RobotDispatchService {

    private final SatelliteRepository satelliteRepository;
    private final RepairRobotRepository robotRepository;
    private final RepairOrderRepository orderRepository;
    private final MotherStationRepository stationRepository;

    public RobotDispatchServiceImpl(SatelliteRepository satelliteRepository,
                                    RepairRobotRepository robotRepository,
                                    RepairOrderRepository orderRepository,
                                    MotherStationRepository stationRepository) {
        this.satelliteRepository = satelliteRepository;
        this.robotRepository = robotRepository;
        this.orderRepository = orderRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<StationDto> getFleet() {
        return stationRepository.findAll().stream()
                .map(DomainMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RepairOrderDto> getOrders() {
        Map<UUID, RepairRobot> robots = robotRepository.findAll().stream()
                .collect(Collectors.toMap(RepairRobot::getId, Function.identity()));
        Map<UUID, Satellite> satellites = satelliteRepository.findAll().stream()
                .collect(Collectors.toMap(Satellite::getId, Function.identity()));

        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
                .filter(order -> robots.containsKey(order.getRobotId()) && satellites.containsKey(order.getSatelliteId()))
                .map(order -> DomainMapper.toDto(order, robots.get(order.getRobotId()), satellites.get(order.getSatelliteId())))
                .toList();
    }

    @Override
    public RepairOrderDto dispatch(DispatchRequestDto request) {
        Satellite satellite = satelliteRepository.findById(request.satelliteId())
                .orElseThrow(() -> new SatelliteNotFoundException(request.satelliteId()));

        RepairRobot robot = robotRepository.findAll().stream()
                .filter(candidate -> candidate.isAvailable() && candidate.canHandle(request.kind()))
                .findFirst()
                .orElseThrow(() -> new RobotUnavailableException("Nenhum robo disponivel para a tarefa " + request.kind() + "."));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime scheduledFor = request.scheduledFor() != null ? request.scheduledFor() : now.plusDays(1);

        RepairOrder order = new RepairOrder(satellite.getId(), robot.getId(), request.kind(), now, scheduledFor, request.notes());
        order.dispatch();
        robot.dispatch();
        satellite.markUnderMaintenance();

        robotRepository.save(robot);
        satelliteRepository.save(satellite);
        orderRepository.save(order);

        return DomainMapper.toDto(order, robot, satellite);
    }

    @Override
    public RepairOrderDto advance(UUID orderId) {
        RepairOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RepairOrderNotFoundException(orderId));
        RepairRobot robot = robotRepository.findById(order.getRobotId())
                .orElseThrow(() -> new RobotUnavailableException("Robo associado a ordem nao encontrado."));
        Satellite satellite = satelliteRepository.findById(order.getSatelliteId())
                .orElseThrow(() -> new SatelliteNotFoundException(order.getSatelliteId()));

        switch (order.getStatus()) {
            case SCHEDULED -> order.dispatch();
            case DISPATCHED -> order.start();
            case IN_PROGRESS -> {
                order.complete(LocalDateTime.now());
                robot.returnToStation();
                satellite.restoreToOperational();
                robotRepository.save(robot);
                satelliteRepository.save(satellite);
            }
            default -> throw new InvalidRepairOrderException("A ordem ja esta " + order.getStatus() + " e nao pode avancar.");
        }

        orderRepository.save(order);
        return DomainMapper.toDto(order, robot, satellite);
    }
}
