package com.tickevent.app.domain.dtos;

import java.time.LocalDate;

public class UserRegistrationDTO {

    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String document;
    private LocalDate birthDate;

    // Construtor vazio (Necessário para frameworks como o Jackson deserializarem o JSON do frontend)
    public UserRegistrationDTO() {
    }

    public UserRegistrationDTO(String name, String email, String password, String phoneNumber, String document, LocalDate birthDate) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.document = document;
        this.birthDate = birthDate;
    }

    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getDocument() { return document; }
    public LocalDate getBirthDate() { return birthDate; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setDocument(String document) { this.document = document; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
}