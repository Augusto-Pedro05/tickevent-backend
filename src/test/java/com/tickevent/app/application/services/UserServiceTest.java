package com.tickevent.app.application.services;

import com.tickevent.app.application.ports.out.PasswordHasher;
import com.tickevent.app.application.ports.out.UserRepository;
import com.tickevent.app.application.service.identity.UserService;
import com.tickevent.app.domain.dtos.UserRegistrationDTO;
import com.tickevent.app.domain.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Deve registrar cliente com sucesso e aplicar hash na senha")
    void shouldRegisterClientSuccessfully() {
        // Arrange
        UserRegistrationDTO dto = new UserRegistrationDTO(
                "João", "joao@email.com", "senha123", "11999999999", "12345678900", LocalDate.of(2000, 1, 1)
        );

        when(userRepository.existsByEmail(dto.email())).thenReturn(false); //[cite: 5]
        when(passwordHasher.hash(dto.password())).thenReturn("hash_seguro"); //[cite: 5]

        // Simula o retorno do banco (pode ser um mock genérico do usuário)
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User savedUser = userService.registerClient(dto);

        // Assert
        assertNotNull(savedUser);
        assertEquals("hash_seguro", savedUser.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve estourar exceção se o email já estiver em uso")
    void shouldThrowExceptionWhenEmailExists() {
        // Arrange
        UserRegistrationDTO dto = new UserRegistrationDTO(
                "João", "joao@email.com", "senha123", "11", "123", LocalDate.now()
        );
        when(userRepository.existsByEmail(dto.email())).thenReturn(true); //[cite: 5]

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.registerClient(dto));
        assertEquals("Email already exists", exception.getMessage()); //[cite: 5]
        verify(userRepository, never()).save(any());
    }
}
