package com.ktv.entity;

public class UserLogin {
    private String account;
    private String password;
    private String role;

    public UserLogin() {}

    public UserLogin(String account, String password, String role) {
        this.account = account;
        this.password = password;
        this.role = role;
    }

    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "UserLogin{" +
                "account='" + account + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}