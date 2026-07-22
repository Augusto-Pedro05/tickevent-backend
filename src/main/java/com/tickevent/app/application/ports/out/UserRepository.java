package com.tickevent.app.application.ports.out;

import com.tickevent.app.domain.models.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    /**
     * Verifica se já existe um usuário cadastrado com o e-mail informado.
     * Usado no fluxo de registro para evitar duplicidade (Fail-Fast).
     */
    boolean existsByEmail(String email);

    /**
     * Salva a instância de domínio User no banco de dados e retorna o objeto salvo.
     */
    User save(User user);

    /**
     * Busca um usuário pelo e-mail.
     * Retorna um Optional para evitar NullPointerException caso o e-mail não exista.
     */
    Optional<User> findByEmail(String email);

    /**
     * Busca um usuário pelo uuid.
     * Retorna um Optional para evitar NullPointerException caso o e-mail não exista.
     */
    Optional<User> findById(UUID id);
}
