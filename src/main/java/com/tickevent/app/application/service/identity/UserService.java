package com.tickevent.app.application.service.identity;

import com.tickevent.app.application.ports.out.PasswordHasher;
import com.tickevent.app.application.ports.out.UserRepository;
import com.tickevent.app.domain.dtos.AdminRegistrationDTO;
import com.tickevent.app.domain.dtos.UserRegistrationDTO;
import com.tickevent.app.domain.models.User;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.tickevent.app.domain.models.User.Role.USER;

public class UserService {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UserService(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    /**
     * Registra um novo cliente final (Ticket Holder) no sistema.
     */
    public User registerClient(UserRegistrationDTO dto){
        if(userRepository.existsByEmail(dto.getEmail())) throw new RuntimeException("Email already exists");
        String hashedPassword = passwordHasher.hash(dto.getPassword());

        User newUser = new User(
                UUID.randomUUID(),
                dto.getName(),
                dto.getEmail(),
                hashedPassword,
                dto.getPhoneNumber(),
                LocalDateTime.now(),
                dto.getDocument(),
                dto.getBirthDate(),
                USER
        );
        return userRepository.save(newUser);
    }


    public void registerAdmin(AdminRegistrationDTO dto){

    }
    public void findUserByEmail(String email){

    }
}
