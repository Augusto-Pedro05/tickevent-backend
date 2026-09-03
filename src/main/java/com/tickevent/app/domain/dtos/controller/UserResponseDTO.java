package com.tickevent.app.domain.dtos.controller;

import com.tickevent.app.domain.models.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String name,
        String email,
        String phoneNumber,
        String role,
        String commercialName,
        Boolean isApproved,
        LocalDateTime createdAt
) {
    public static UserResponseDTO fromDomain(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole() != null ? user.getRole().name() : null,
                user.getCommercialName(),
                user.getIsApproved(),
                user.getCreatedAt()
        );
    }
}
