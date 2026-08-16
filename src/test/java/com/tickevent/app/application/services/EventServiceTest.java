package com.tickevent.app.application.services;

import com.tickevent.app.application.ports.out.EventRepository;
import com.tickevent.app.application.ports.out.UserRepository;
import com.tickevent.app.application.service.catalog.EventService;
import com.tickevent.app.domain.dtos.EventCreationDTO;
import com.tickevent.app.domain.dtos.LocationDTO;
import com.tickevent.app.domain.models.Event;
import com.tickevent.app.domain.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    @DisplayName("Deve bloquear a criação de evento se o usuário não for ADMIN")
    void shouldThrowExceptionWhenUserIsNotAdmin() {
        // Arrange
        UUID requesterId = UUID.randomUUID();
        EventCreationDTO dto = mock(EventCreationDTO.class);

        User normalUser = mock(User.class);
        when(normalUser.getRole()).thenReturn(User.Role.USER); // Simulando usuário comum

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(normalUser)); //[cite: 2]

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> eventService.createEvent(requesterId, dto));
        assertEquals("Unauthorized access", exception.getMessage()); //[cite: 2]
        verify(eventRepository, never()).save(any());
    }
}