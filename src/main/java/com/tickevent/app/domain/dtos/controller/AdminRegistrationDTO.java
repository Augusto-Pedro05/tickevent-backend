package com.tickevent.app.domain.dtos.controller;

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
