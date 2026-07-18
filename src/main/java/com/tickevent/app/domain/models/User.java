package com.tickevent.app.domain.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public class User {
    private enum Role {
        SUPER_ADMIN,
        ADMIN,
        USER
    }
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private Role role;
    private String document;
    private LocalDate birthDate;
    private String commercialName;
    private String bankAccountDetails;
    private Boolean isApproved;
    private List<Event> managedEvents;
}
