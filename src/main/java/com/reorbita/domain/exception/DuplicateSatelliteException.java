package com.reorbita.domain.exception;

public class DuplicateSatelliteException extends DomainException {

    public DuplicateSatelliteException(String noradId) {
        super("Ja existe um satelite registrado com o NORAD ID " + noradId + ".");
    }
}
