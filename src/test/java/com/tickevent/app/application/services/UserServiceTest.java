package com.tickevent.app.application.services;

import com.tickevent.app.application.ports.out.PasswordHasher;
import com.tickevent.app.application.ports.out.UserRepository;
import com.tickevent.app.application.service.identity.UserService;
import com.tickevent.app.domain.dtos.controller.UserRegistrationDTO;
import com.tickevent.app.domain.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Classe de testes unitários para a gestão de identidade e usuários (UserService).
 * Responsável por garantir que o fluxo de registro e autenticação cumpra os requisitos
 * de segurança.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should successfully register a customer and hash the password")
    void shouldRegisterClientSuccessfully() {
        // Arrange
        UserRegistrationDTO dto = mock(UserRegistrationDTO.class);

        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(passwordHasher.hash(dto.password())).thenReturn("hash_seguro");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User savedUser = userService.registerClient(dto);

        // Assert
        assertNotNull(savedUser);
        assertEquals("hash_seguro", savedUser.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("An exception should be thrown if the email is already in use.")
    void shouldThrowExceptionWhenEmailExists() {
        // Arrange
        UserRegistrationDTO dto = mock(UserRegistrationDTO.class);

        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.registerClient(dto));
        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
}
