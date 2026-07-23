package com.tickevent.app.domain.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TicketCategoryCreationDTO(
        String name,
        String description,
        BigDecimal price,
        Integer totalLimit,
        LocalDateTime salesStartDate,
        LocalDateTime salesEndDate
) {
}