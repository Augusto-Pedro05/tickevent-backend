package com.tickevent.app.domain.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Event {
    private enum Status {
        DRAFT,
        PUBLISHED,
        CANCELED,
        FINISHED,
        SUSPENDED
    }

    private UUID id;
    private String title;
    private String description;
    private String bannerUrl;
    private String category;
    private Integer maxCapacity;
    private String locationText;
    private Integer maxTicketsPerUser;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Status status;
    private List<TicketCategory> ticketCategories;
}
