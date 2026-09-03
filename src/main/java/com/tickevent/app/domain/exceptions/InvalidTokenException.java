package com.tickevent.app.domain.exceptions;

public class InvalidTokenException extends BusinessException {
    public InvalidTokenException() {
        super("Invalid or expired token");
    }
}
