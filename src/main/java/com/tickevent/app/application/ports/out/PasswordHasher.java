package com.tickevent.app.application.ports.out;

public interface PasswordHasher {

    /**
     * Recebe uma senha em texto plano e retorna
     * o hash criptografado irReversível.
     * Usado no fluxo de registro.
     */
    String hash(String plainPassword);

    /**
     * Compara uma senha em texto plano com um hash salvo no banco de dados.
     * Retorna true se a senha estiver correta, ou false caso contrário.
     * Será essencial para a função authenticate() do AuthService.
     */
    boolean matches(String plainPassword, String hashedPassword);
}
