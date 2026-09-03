package com.tickevent.app.domain.dtos.controller;

import java.time.LocalDate;

public record UserRegistrationDTO(
        String name,
        String email,
        String password,
        String phoneNumber,
        String document,
        LocalDate birthDate
) {
}