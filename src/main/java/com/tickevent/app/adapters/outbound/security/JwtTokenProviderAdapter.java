package com.tickevent.app.adapters.outbound.security;

import com.tickevent.app.application.ports.out.TokenProvider;
import com.tickevent.app.domain.models.User;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProviderAdapter implements TokenProvider {

    // TODO: make the tokenProvider
    @Override
    public String generateToken(User user) {
        // Estrutura inicial que substituirá o retorno de mock
        // pela implementação real de assinatura HMAC256.
        return "mock-jwt-token-for-" + user.getId();
    }
    /*
    @Override
    public String extractSubject(String token) {
        // Extrairá o ID do usuário de dentro da String criptografada
        return "mock-subject-id";
    }

    @Override
    public boolean isTokenValid(String token) {
        // Validará tempo de expiração e assinatura
        return true;
    }*/
}