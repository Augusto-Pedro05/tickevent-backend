package com.tickevent.app.application.service.catalog;

import com.tickevent.app.application.ports.out.EventRepository;
import com.tickevent.app.application.ports.out.TicketCategoryRepository;
import com.tickevent.app.application.ports.out.UserRepository;
import com.tickevent.app.domain.dtos.TicketCategoryCreationDTO;
import com.tickevent.app.domain.dtos.TicketCategoryUpdateDTO;
import com.tickevent.app.domain.models.Event;
import com.tickevent.app.domain.models.TicketCategory;
import com.tickevent.app.domain.models.User;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import static com.tickevent.app.application.service.utils.PatchUtils.updateIfPresent;
import static com.tickevent.app.application.service.utils.PatchUtils.verifyEventOwnership;

@RequiredArgsConstructor
public class TicketCategoryService {

    private final TicketCategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public TicketCategory createCategory(UUID eventId, UUID requesterId, TicketCategoryCreationDTO dto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found."));
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        verifyEventOwnership(requester, event);

        TicketCategory newCategory = new TicketCategory(
                UUID.randomUUID(),
                dto.name(),
                dto.description()
        );

        return categoryRepository.save(newCategory);
    }

    public TicketCategory updateCategory(UUID eventId, UUID categoryId, UUID requesterId, TicketCategoryUpdateDTO dto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found."));
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        verifyEventOwnership(requester, event);

        TicketCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Ticket category not found."));

        updateIfPresent(dto.name(), category::setName);
        updateIfPresent(dto.description(), category::setDescription);

        return categoryRepository.save(category);
    }

    public void deleteCategory(UUID eventId, UUID categoryId, UUID requesterId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found."));
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        verifyEventOwnership(requester, event);

        TicketCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Ticket category not found."));

        boolean hasSoldTickets = category.getBatches().stream()
                .anyMatch(batch -> batch.getAvailableQuantity() < batch.getTotalLimit());

        if (hasSoldTickets) {
            throw new RuntimeException("Cannot delete a ticket category that already has sold tickets in one of its batches.");
        }

        categoryRepository.delete(category);
    }

}