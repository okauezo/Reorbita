package com.reorbita.domain.exception;

import java.util.UUID;

public class SatelliteNotFoundException extends DomainException {

    public SatelliteNotFoundException(UUID id) {
        super("Satelite " + id + " nao encontrado.");
    }
}
