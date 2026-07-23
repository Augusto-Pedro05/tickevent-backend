package com.tickevent.app.application.service.identity;

import com.tickevent.app.application.ports.out.PasswordHasher;
import com.tickevent.app.application.ports.out.UserRepository;
import com.tickevent.app.domain.dtos.AdminRegistrationDTO;
import com.tickevent.app.domain.dtos.UserRegistrationDTO;
import com.tickevent.app.domain.models.User;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    /**
     * Registra um novo cliente final (Ticket Holder) no sistema.
     */
    public User registerClient(UserRegistrationDTO dto){
        if(userRepository.existsByEmail(dto.email())) throw new RuntimeException("Email already exists");
        String hashedPassword = passwordHasher.hash(dto.password());

        User newUser = new User(
                UUID.randomUUID(),
                dto.name(),
                dto.email(),
                hashedPassword,
                dto.phoneNumber(),
                LocalDateTime.now(),
                dto.document(),
                dto.birthDate()
        );
        return userRepository.save(newUser);
    }


    public User registerAdmin(AdminRegistrationDTO dto){
        if(userRepository.existsByEmail(dto.email())) throw new RuntimeException("Email already exists");
        String hashedPassword = passwordHasher.hash(dto.password());

        User newAdmin = new User(
                UUID.randomUUID(),
                dto.name(),
                dto.email(),
                hashedPassword,
                dto.phoneNumber(),
                LocalDateTime.now(),
                dto.document(),
                dto.commercialName(),
                dto.bankAccountDetails(),
                false
        );
        return userRepository.save(newAdmin);
    }
    public User findUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("Unregistered email"));
    }
}
