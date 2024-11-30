package com.example.acmeplex.paymentsystem.entity;

public class Card {

    private String email;
    private String cardNumber;
    private int expireYear;
    private int expireMonth;
    private String name;
    private String type;
    private String cvv;

    public Card(String email, String cardNumber, int expireYear, int expireMonth, String cvv, String name, String type) {
        this.email = email;
        this.cardNumber = cardNumber;
        this.expireYear = expireYear;
        this.expireMonth = expireMonth;
        this.name = name;
        this.type = type;
        this.cvv = cvv;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCardNumber() {
        return this.cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getExpireYear() {
        return this.expireYear;
    }

    public void setExpireYear(int expireYear) {
        this.expireYear = expireYear;
    }

    public int getExpireMonth() {
        return this.expireMonth;
    }

    public void setExpireMonth(int expireMonth) {
        this.expireMonth = expireMonth;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCvv() {
        return this.cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }


    
}
