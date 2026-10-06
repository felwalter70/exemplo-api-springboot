package com.course.cursospring.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;


@Getter 
@Setter 
@RequiredArgsConstructor 
public class ErrorResponse {
    private final int status;
    private final String message;
    private String stackTrace;
    private List<ValidationError> errors;


    @Getter 
    @Setter 
    @RequiredArgsConstructor 
    private static class ValidationError {
        private final String field;
        private final String message;
    }

    public void addValidationError(String field, String message) {
        if (Objects.isNull(errors))
            errors = new ArrayList<>();
        
        errors.add(new ValidationError(field, message));
    }
}
