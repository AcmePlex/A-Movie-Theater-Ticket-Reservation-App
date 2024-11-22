package com.example.acmeplex.paymentsystem.entity;

import java.sql.Timestamp;

public class Payment {
    private String email;
    private String method;
    private boolean status;
    private int id;
    private double amount;
    private Timestamp lastUpdateTime;
    private String type;

    public Payment() {
    }

    public Payment(String email, String method, boolean status, int id, double amount, Timestamp lastUpdateTime, String type) {
        this.email = email;
        this.method = method;
        this.status = status;
        this.id = id;
        this.amount = amount;
        this.lastUpdateTime = lastUpdateTime;
        this.type = type;
    }


    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isStatus() {
        return this.status;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }



   
}
