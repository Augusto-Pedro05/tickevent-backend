package com.tickevent.app.domain.dtos;

import com.tickevent.app.domain.models.TicketCategory;

import java.time.LocalDateTime;
import java.util.List;

public record EventCreationDTO(
        String title,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate,
        LocationDTO location
) {
}
