package com.tickevent.app.domain.dtos.controller;

public record AuthResponseDTO(
        String token,
        String type
) {
    public AuthResponseDTO(String token) {
        this(token, "Bearer");
    }
}
