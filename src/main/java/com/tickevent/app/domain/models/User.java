package com.tickevent.app.domain.models;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class User {

    public enum Role {
        SUPER_ADMIN,
        ADMIN,
        USER
    }

    @Setter(AccessLevel.NONE)
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

    public User() {
    }

    public User(UUID id, String name, String email, String password, String phoneNumber,
                LocalDateTime createdAt, String document, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.document = document;
        this.birthDate = birthDate;
        this.role = Role.USER;
        this.commercialName = null;
        this.bankAccountDetails = null;
        this.isApproved = null;
    }

    public User(UUID id, String name, String email, String password, String phoneNumber,
                LocalDateTime createdAt, String document, String commercialName,
                String bankAccountDetails, Boolean isApproved) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.document = document;
        this.commercialName = commercialName;
        this.bankAccountDetails = bankAccountDetails;
        this.isApproved = isApproved;
        this.role = Role.ADMIN;
        this.birthDate = null;
    }
}
