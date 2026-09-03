package com.tickevent.app.domain.exceptions;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String resource) {
        super(resource + " not found");
    }
}
