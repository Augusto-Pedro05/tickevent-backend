package com.tickevent.app.domain.exceptions;

public class EmailAlreadyExistsException extends BusinessException {
    public EmailAlreadyExistsException() {
        super("Email already exists");
    }
}
