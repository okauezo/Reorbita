package com.reorbita.web;

import com.reorbita.domain.exception.AlertNotFoundException;
import com.reorbita.domain.exception.DomainException;
import com.reorbita.domain.exception.DuplicateSatelliteException;
import com.reorbita.domain.exception.RepairOrderNotFoundException;
import com.reorbita.domain.exception.RobotUnavailableException;
import com.reorbita.domain.exception.SatelliteNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({SatelliteNotFoundException.class, AlertNotFoundException.class, RepairOrderNotFoundException.class})
    public ResponseEntity<ProblemDetail> handleNotFound(DomainException ex) {
        return build(HttpStatus.NOT_FOUND, "Recurso nao encontrado", ex.getMessage());
    }

    @ExceptionHandler(DuplicateSatelliteException.class)
    public ResponseEntity<ProblemDetail> handleDuplicate(DuplicateSatelliteException ex) {
        return build(HttpStatus.CONFLICT, "Conflito de registro", ex.getMessage());
    }

    @ExceptionHandler(RobotUnavailableException.class)
    public ResponseEntity<ProblemDetail> handleRobotUnavailable(RobotUnavailableException ex) {
        return build(HttpStatus.CONFLICT, "Recurso indisponivel", ex.getMessage());
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ProblemDetail> handleDomain(DomainException ex) {
        return build(HttpStatus.BAD_REQUEST, "Regra de dominio violada", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArgument(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, "Requisicao invalida", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Erro de validacao");
        problem.setDetail("Um ou mais campos sao invalidos.");
        problem.setProperty("errors", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpected(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno",
                "Ocorreu uma falha inesperada e foi registrada para analise.");
    }

    private ResponseEntity<ProblemDetail> build(HttpStatus status, String title, String detail) {
        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(title);
        problem.setDetail(detail);
        return ResponseEntity.status(status).body(problem);
    }
}
