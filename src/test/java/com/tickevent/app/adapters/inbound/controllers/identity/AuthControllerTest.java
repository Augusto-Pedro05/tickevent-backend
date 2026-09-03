package com.tickevent.app.adapters.inbound.controllers.identity;

import com.tickevent.app.application.service.identity.AuthService;
import com.tickevent.app.application.service.identity.UserService;
import com.tickevent.app.domain.dtos.controller.AdminRegistrationDTO;
import com.tickevent.app.domain.dtos.controller.LoginDTO;
import com.tickevent.app.domain.dtos.controller.UserRegistrationDTO;
import com.tickevent.app.domain.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    @DisplayName("Should return 200 OK and token when login is successful")
    void shouldLoginSuccessfully() throws Exception {
        LoginDTO dto = new LoginDTO("user@test.com", "password123");
        when(authService.authenticate(any(LoginDTO.class))).thenReturn("jwt-token-sample");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-sample"))
                .andExpect(jsonPath("$.type").value("Bearer"));

        verify(authService).authenticate(any(LoginDTO.class));
    }

    @Test
    @DisplayName("Should return 201 Created and user details when client registration succeeds")
    void shouldRegisterClientSuccessfully() throws Exception {
        UserRegistrationDTO dto = new UserRegistrationDTO(
                "John Doe",
                "john@test.com",
                "password123",
                "11999998888",
                "12345678901",
                LocalDate.of(1990, 1, 1)
        );

        User savedUser = new User(
                UUID.randomUUID(),
                dto.name(),
                dto.email(),
                "hashed-password",
                dto.phoneNumber(),
                LocalDateTime.now(),
                dto.document(),
                dto.birthDate()
        );

        when(userService.registerClient(any(UserRegistrationDTO.class))).thenReturn(savedUser);

        mockMvc.perform(post("/api/v1/auth/register/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@test.com"))
                .andExpect(jsonPath("$.role").value("USER"));

        verify(userService).registerClient(any(UserRegistrationDTO.class));
    }

    @Test
    @DisplayName("Should return 201 Created and admin details when admin registration succeeds")
    void shouldRegisterAdminSuccessfully() throws Exception {
        AdminRegistrationDTO dto = new AdminRegistrationDTO(
                "Event Producer",
                "producer@test.com",
                "password123",
                "11999997777",
                "12345678000199",
                "Producer Events Inc",
                "Bank Account 123"
        );

        User savedAdmin = new User(
                UUID.randomUUID(),
                dto.name(),
                dto.email(),
                "hashed-password",
                dto.phoneNumber(),
                LocalDateTime.now(),
                dto.document(),
                dto.commercialName(),
                dto.bankAccountDetails(),
                false
        );

        when(userService.registerAdmin(any(AdminRegistrationDTO.class))).thenReturn(savedAdmin);

        mockMvc.perform(post("/api/v1/auth/register/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Event Producer"))
                .andExpect(jsonPath("$.email").value("producer@test.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.commercialName").value("Producer Events Inc"))
                .andExpect(jsonPath("$.isApproved").value(false));

        verify(userService).registerAdmin(any(AdminRegistrationDTO.class));
    }
}
