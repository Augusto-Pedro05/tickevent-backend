package com.tickevent.app.adapters.inbound.controllers.identity;

import com.tickevent.app.application.service.identity.AuthService;
import com.tickevent.app.application.service.identity.UserService;
import com.tickevent.app.domain.dtos.controller.AdminRegistrationDTO;
import com.tickevent.app.domain.dtos.controller.AuthResponseDTO;
import com.tickevent.app.domain.dtos.controller.LoginDTO;
import com.tickevent.app.domain.dtos.controller.UserRegistrationDTO;
import com.tickevent.app.domain.dtos.controller.UserResponseDTO;
import com.tickevent.app.domain.models.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO dto) {
        String token = authService.authenticate(dto);
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    @PostMapping("/register/client")
    public ResponseEntity<UserResponseDTO> registerClient(@Valid @RequestBody UserRegistrationDTO dto) {
        User registeredUser = userService.registerClient(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.fromDomain(registeredUser));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<UserResponseDTO> registerAdmin(@Valid @RequestBody AdminRegistrationDTO dto) {
        User registeredAdmin = userService.registerAdmin(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.fromDomain(registeredAdmin));
    }
}
