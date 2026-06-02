package com.reorbita.domain.exception;

import java.util.UUID;

public class AlertNotFoundException extends DomainException {

    public AlertNotFoundException(UUID id) {
        super("Alerta " + id + " nao encontrado.");
    }
}
