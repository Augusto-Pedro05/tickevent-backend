package com.tickevent.app.application.ports.out;

import com.tickevent.app.domain.models.Event;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository {
    Event save (Event event);
    Optional<Event> findById(UUID eventId);
}
