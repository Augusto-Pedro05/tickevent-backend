package com.tickevent.app.application.service.utils;

import com.tickevent.app.domain.dtos.LocationDTO;
import com.tickevent.app.domain.models.Event;
import com.tickevent.app.domain.models.User;

import java.util.function.Consumer;

public class PatchUtils {

    public static <T> void updateIfPresent(T value, Consumer<T> setter) {
        if (value == null) {
            return;
        }
        if (value instanceof String str && str.isBlank()) {
            return;
        }
        setter.accept(value);
    }

    public static boolean isLocationIncomplete(LocationDTO loc) {
        return loc.venueName() == null || loc.venueName().isBlank() ||
                loc.street() == null || loc.street().isBlank() ||
                loc.number() == null || loc.number().isBlank() ||
                loc.city() == null || loc.city().isBlank() ||
                loc.state() == null || loc.state().isBlank();
    }

    public static void verifyEventOwnership(User requester, Event event) {
        if (requester.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Access denied: only users with admin role perform this action");
        }
        if (!event.getCreator().getId().equals(requester.getId())) {
            throw new RuntimeException("Access denied: the requester is not owner");
        }
    }
}
