package com.example.crmsystem.entity;

import java.time.LocalDateTime;

public class Seller {
    private Long id;
    private String name;
    private String contactInfo;
    private LocalDateTime registrationDate;
    private boolean isDeleted;

    public Seller() {}

    public Seller(Long id, String name, String contactInfo, LocalDateTime registrationDate, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.registrationDate = registrationDate;
        this.isDeleted = isDeleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
    @Override
    public String toString() {
        return "Seller{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
