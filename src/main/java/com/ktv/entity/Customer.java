package com.ktv.entity;

public class Customer {
    private String account;
    private String name;
    private String gender;
    private String phone;

    public Customer() {}

    public Customer(String account, String name, String gender, String phone) {
        this.account = account;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
    }

    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return "Customer{" +
                "account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}