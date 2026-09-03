package com.tickevent.app.domain.exceptions;

public class UnauthorizedAccessException extends BusinessException {
    public UnauthorizedAccessException() {
        super("Unauthorized access");
    }
}
