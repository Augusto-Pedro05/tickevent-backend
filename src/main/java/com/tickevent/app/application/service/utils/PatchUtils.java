package com.tickevent.app.application.service.utils;

import com.tickevent.app.domain.dtos.LocationDTO;

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
}
