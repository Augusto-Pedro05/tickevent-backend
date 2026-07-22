package com.tickevent.app.domain.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TicketCategoryRequestDTO(
        String name,
        String description,
        Integer totalCapacity,
        BigDecimal price,
        LocalDateTime validUntil
) {
}
