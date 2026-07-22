package com.tickevent.app.domain.models;

public class Location {
    private String venueName;
    private String street;
    private String number;
    private String city;
    private String state;

    public Location(String venueName, String street, String number,
                    String city, String state) {
        this.venueName = venueName;
        this.street = street;
        this.number = number;
        this.city = city;
        this.state = state;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }
}
