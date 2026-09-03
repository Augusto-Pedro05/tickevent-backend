package com.tickevent.app.adapters.inbound.middlewares;

import com.tickevent.app.domain.dtos.middleware.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleRuntimeException(RuntimeException ex) {
        String message = ex.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (message != null) {
            if (message.contains("Email already exists")) {
                status = HttpStatus.CONFLICT;
            } else if (message.contains("Invalid credentials") || message.contains("Unregistered email")) {
                status = HttpStatus.UNAUTHORIZED;
            } else if (message.contains("Unauthorized access")) {
                status = HttpStatus.FORBIDDEN;
            } else if (message.contains("not found") || message.contains("Not found")) {
                status = HttpStatus.NOT_FOUND;
            }
        }

        return ResponseEntity.status(status).body(new ErrorResponseDTO(message, status.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .orElse("Validation failed");

        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(new ErrorResponseDTO(errorMessage, status.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(new ErrorResponseDTO("An unexpected error occurred", status.value()));
    }
}
