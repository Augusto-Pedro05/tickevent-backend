package com.tickevent.app.domain.dtos;

public record LocationDTO(
        String venueName,
        String street,
        String number,
        String city,
        String state
) {
}
