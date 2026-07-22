package com.tickevent.app.application.ports.out;

import com.tickevent.app.domain.models.User;

public interface TokenProvider {
    String generateToken(User user);
}
