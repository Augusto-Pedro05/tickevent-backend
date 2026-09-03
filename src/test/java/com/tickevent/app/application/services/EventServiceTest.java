package com.tickevent.app.application.services;

import com.tickevent.app.application.ports.out.EventRepository;
import com.tickevent.app.application.ports.out.UserRepository;
import com.tickevent.app.application.service.catalog.EventService;
import com.tickevent.app.domain.dtos.controller.EventCreationDTO;
import com.tickevent.app.domain.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Classe de testes unitários para o serviço de catálogo de eventos (EventService).
 * Utiliza o Mockito para isolar a camada de Aplicação das dependências de infraestrutura (Banco de Dados).
 */
@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    @DisplayName("It must block event creation if the user is not an ADMIN")
    void shouldThrowExceptionWhenUserIsNotAdmin() {
        // Arrange
        UUID requesterId = UUID.randomUUID();

        EventCreationDTO dto = mock(EventCreationDTO.class);
        User normalUser = mock(User.class);

        when(normalUser.getRole()).thenReturn(User.Role.USER);
        when(userRepository.findById(requesterId)).thenReturn(Optional.of(normalUser));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> eventService.createEvent(requesterId, dto));
        assertEquals("Unauthorized access", exception.getMessage());
        verify(eventRepository, never()).save(any());
    }
}