package com.reorbita.application.service;

import com.reorbita.application.dto.PredictionDto;

import java.util.UUID;

public interface PredictiveMaintenanceService {

    PredictionDto analyze(UUID satelliteId);
}
