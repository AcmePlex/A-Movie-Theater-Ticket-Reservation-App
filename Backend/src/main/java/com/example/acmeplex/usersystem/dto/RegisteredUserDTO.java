package com.example.acmeplex.usersystem.dto;

import jakarta.validation.constraints.NotBlank;

public class RegisteredUserDTO extends UserDTO {
    
    @NotBlank(message = "Credit Card Info is mandatory")
    private String creditCardInfo;
    
    private boolean activeSubscription;

    // Default constructor
    public RegisteredUserDTO() {}

    // All-args constructor
    public RegisteredUserDTO(Long id, String name, String email, String address, String creditCardInfo, boolean activeSubscription) {
        super(id, name, email, address);
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
