package com.course.cursospring.exceptions;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.course.cursospring.services.exceptions.AuthorizationException;
import com.course.cursospring.services.exceptions.DataBindingViolationException;

import jakarta.servlet.http.HttpServletRequest;

import java.nio.file.AccessDeniedException;

import javax.security.sasl.AuthenticationException;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice 
public class GlobalExceptionHandler {
    @Value ("${spring.web.error.include-exception}")
    private boolean printStackTrace;

    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        detail.setTitle("Erro de validação");
        detail.setDetail("Um ou mais campos estão inválidos");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(detail);
    }

    @ExceptionHandler (ObjectNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFoundException(ObjectNotFoundException ex) {
        ProblemDetail details = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        
        details.setTitle("Resource not found!");
        details.setDetail("Details: " + ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(details);
    }

    @ExceptionHandler (DataBindingViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataBindingViolationException(DataBindingViolationException ex) {
        ProblemDetail details = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        details.setTitle("Data binding violation");
        details.setDetail("Details: " + ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(details);
    }

    @ExceptionHandler (AuthenticationException.class)
    public ResponseEntity<ProblemDetail> handleAuthenticationException(AuthenticationException ex) {
        ProblemDetail details = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);

        details.setTitle("Authentication error");
        details.setDetail("Details: " + ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(details);
    }

    @ExceptionHandler (AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDeniedException(AccessDeniedException ex) {
        ProblemDetail details = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);

        details.setTitle("Access denied");
        details.setDetail("Details: " + ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(details);
    }

    @ExceptionHandler (AuthorizationException.class)
    public ResponseEntity<ProblemDetail> handleAuthorizationException(AuthorizationException ex) {
        ProblemDetail details = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);

        details.setTitle("Authorization error");
        details.setDetail("Details: " + ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(details);
    }

    @ExceptionHandler (Exception.class)
    public ResponseEntity<ProblemDetail> handleUnhandledExceptions(Exception ex) {
        ProblemDetail details = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        details.setTitle("Unknown exception (Internal server error)");
        details.setDetail("Details: " + ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(details);
    }
}
