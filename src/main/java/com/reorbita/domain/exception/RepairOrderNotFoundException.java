package com.reorbita.domain.exception;

import java.util.UUID;

public class RepairOrderNotFoundException extends DomainException {

    public RepairOrderNotFoundException(UUID id) {
        super("Ordem de reparo " + id + " nao encontrada.");
    }
}
