package com.tickevent.app.domain.dtos;

import java.time.LocalDate;

public record AdminRegistrationDTO(
        String name,
        String email,
        String password,
        String phoneNumber,
        String document,
        String commercialName,
        String bankAccountDetails
) {
}
