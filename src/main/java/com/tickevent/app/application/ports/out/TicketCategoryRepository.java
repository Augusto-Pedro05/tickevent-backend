package com.tickevent.app.application.ports.out;

import com.tickevent.app.domain.models.TicketCategory;

import java.util.Optional;
import java.util.UUID;

public interface TicketCategoryRepository {
    TicketCategory save(TicketCategory newCategory);

    Optional<TicketCategory> findById(UUID categoryId);

    void delete(TicketCategory category);
}
