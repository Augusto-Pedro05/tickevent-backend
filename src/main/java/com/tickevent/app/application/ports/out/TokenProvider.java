package com.tickevent.app.application.ports.out;

import com.tickevent.app.domain.models.User;

public interface TokenProvider {
    /**
     * Gera um token JWT assinado contendo as claims do usuário.
     */
    String generateToken(User user);

    /**
     * Extrai o subject (userId) de dentro do token JWT.
     */
    String extractSubject(String token);

    /**
     * Extrai a role do usuário de dentro do token JWT.
     */
    String extractRole(String token);

    /**
     * Valida a assinatura e a expiração do token.
     * Retorna true se o token for válido, false caso contrário.
     */
    boolean isTokenValid(String token);
}
