package com.tickevent.app.domain.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private String venueName;
    private String street;
    private String number;
    private String city;
    private String state;
}