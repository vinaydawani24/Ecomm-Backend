package com.vinay.ecomm.dto;

public class PaymentOrderRequest {
    private String name;
    private String email;
    private String phone;
    private double amt;

    // getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public double getAmt() { return amt; }
    public void setAmt(double amt) { this.amt = amt; }
}
