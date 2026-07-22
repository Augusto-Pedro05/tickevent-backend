package com.tickevent.app.domain.dtos;

import java.time.LocalDateTime;

public record EventUpdateDTO(
        String title,
        String description,
        String bannerUrl,
        String category,
        Integer maxCapacity,
        LocationDTO location,
        Integer maxTicketsPerUser,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
) {
}