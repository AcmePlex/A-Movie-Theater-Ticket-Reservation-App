package com.example.acmeplex.usersystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "registered_users")
public class RegisteredUser extends User {

    private String creditCardInfo;
    private boolean activeSubscription;

    // Default constructor
    public RegisteredUser() {}

    // All-args constructor
    public RegisteredUser(String name, String email, String address, String creditCardInfo, boolean activeSubscription) {
        super(name, email, address);
        this.creditCardInfo = creditCardInfo;
        this.activeSubscription = activeSubscription;
    }

    // Getters and Setters
    public String getCreditCardInfo() {
        return creditCardInfo;
    }

    public void setCreditCardInfo(String creditCardInfo) {
        this.creditCardInfo = creditCardInfo;
    }

    public boolean isActiveSubscription() {
        return activeSubscription;
    }

    public void setActiveSubscription(boolean activeSubscription) {
        this.activeSubscription = activeSubscription;
    }
}
