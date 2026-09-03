package com.tickevent.app.domain.dtos.middleware;

import java.time.LocalDateTime;

public record ErrorResponseDTO (
        String message,
        int httpStatus,
        LocalDateTime timestamp
){
    public ErrorResponseDTO(String message, int status) {
        this(message, status, LocalDateTime.now());
    }
}
