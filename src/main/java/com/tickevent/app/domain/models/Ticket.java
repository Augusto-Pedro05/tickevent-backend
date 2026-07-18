package com.tickevent.app.domain.models;

import java.util.UUID;

public class Ticket {
    private enum Status {
        VALID,
        USED,
        REVOKED
    }

    private UUID id;
    private String validationToken;
    private Status status;
    private TicketCategory category;
}