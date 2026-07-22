package com.tickevent.app.application.service.identity;

import com.tickevent.app.application.ports.out.PasswordHasher;
import com.tickevent.app.application.ports.out.TokenProvider;
import com.tickevent.app.domain.dtos.LoginDTO;
import com.tickevent.app.domain.models.User;

public class AuthService {

    private final UserService userService;
    private final PasswordHasher passwordHasher;
    private final TokenProvider tokenProvider;

    public AuthService(UserService userService, PasswordHasher passwordHasher, TokenProvider tokenProvider){
        this.userService = userService;
        this.passwordHasher = passwordHasher;
        this.tokenProvider = tokenProvider;
    }

    public String authenticate(LoginDTO dto){
        User user = userService.findUserByEmail(dto.email());
        if(!passwordHasher.matches(dto.password(), user.getPassword())){
            throw new RuntimeException("Invalid credentials");
        }
        return tokenProvider.generateToken(user);
    }
}
