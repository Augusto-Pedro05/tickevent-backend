package com.tickevent.app.domain.exceptions;

/**
 * Classe base abstrata para todas as exceções de negócio do domínio.
 * Permite tratamento centralizado no GlobalExceptionHandler.
 */
public abstract class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
