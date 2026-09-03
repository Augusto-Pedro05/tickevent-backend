package com.tickevent.app.domain.dtos.controller;

import java.time.LocalDateTime;

public record EventCreationDTO(
        String title,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}