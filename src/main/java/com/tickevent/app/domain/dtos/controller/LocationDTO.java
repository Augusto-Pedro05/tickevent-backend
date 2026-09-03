package com.tickevent.app.domain.dtos.controller;

public record LocationDTO(
        String venueName,
        String street,
        String number,
        String city,
        String state
) {
}
