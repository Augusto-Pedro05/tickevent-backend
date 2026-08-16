package com.tickevent.app.application.services;

import com.tickevent.app.application.ports.out.EventRepository;
import com.tickevent.app.application.ports.out.TicketCategoryRepository;
import com.tickevent.app.application.ports.out.UserRepository;
import com.tickevent.app.application.service.catalog.TicketCategoryService;
import com.tickevent.app.domain.models.Event;
import com.tickevent.app.domain.models.TicketBatch;
import com.tickevent.app.domain.models.TicketCategory;
import com.tickevent.app.domain.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketCategoryServiceTest {

    @Mock
    private TicketCategoryRepository categoryRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TicketCategoryService ticketCategoryService;

    @Test
    @DisplayName("Deve impedir a exclusão da categoria se houver ingressos vendidos nos lotes")
    void shouldPreventDeletionWhenTicketsAreSold() {
        // Arrange
        UUID eventId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID requesterId = UUID.randomUUID();

        Event mockEvent = mock(Event.class);
        User mockRequester = mock(User.class);
        TicketCategory mockCategory = mock(TicketCategory.class);
        TicketBatch mockBatch = mock(TicketBatch.class);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(mockEvent)); //[cite: 3]
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(mockRequester)); //[cite: 3]
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(mockCategory)); //[cite: 3]

        // Simula que o lote tem ingressos vendidos (Disponível é menor que o Limite Total)
        when(mockBatch.getAvailableQuantity()).thenReturn(50); //[cite: 3]
        when(mockBatch.getTotalLimit()).thenReturn(100); //[cite: 3]
        when(mockCategory.getBatches()).thenReturn(List.of(mockBatch)); //[cite: 3]
        when(mockRequester.getRole()).thenReturn(User.Role.ADMIN);
        when(mockEvent.getCreator()).thenReturn(mockRequester);
        when(mockRequester.getId()).thenReturn(requesterId);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                ticketCategoryService.deleteCategory(eventId, categoryId, requesterId)
        );

        assertEquals("Cannot delete a ticket category that already has sold tickets in one of its batches.", exception.getMessage()); //[cite: 3]
        verify(categoryRepository, never()).delete(any());
    }
}