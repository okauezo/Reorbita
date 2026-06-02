package com.reorbita.application.service;

import com.reorbita.application.dto.AlertDto;

import java.util.List;
import java.util.UUID;

public interface AlertService {

    List<AlertDto> getActive();

    List<AlertDto> getForSatellite(UUID satelliteId);

    AlertDto acknowledge(UUID alertId);
}
