package com.tickevent.app.domain.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TicketCategory {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer totalLimit;
    private Integer availableQuantity;
    private LocalDateTime salesStartDate;
    private LocalDateTime salesEndDate;
}
