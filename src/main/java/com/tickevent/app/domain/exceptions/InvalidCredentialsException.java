package com.tickevent.app.domain.exceptions;

/**
 * Lançada tanto para credenciais incorretas quanto para e-mail não cadastrado,
 * de forma a impedir enumeração de contas.
 */
public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException() {
        super("Invalid credentials");
    }
}
