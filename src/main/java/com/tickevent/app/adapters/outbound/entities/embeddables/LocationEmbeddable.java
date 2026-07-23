package com.tickevent.app.adapters.outbound.entities.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationEmbeddable {

    @Column(name = "location_venue_name")
    private String venueName;

    @Column(name = "location_street")
    private String street;

    @Column(name = "location_number")
    private String number;

    @Column(name = "location_city")
    private String city;

    @Column(name = "location_state")
    private String state;
}