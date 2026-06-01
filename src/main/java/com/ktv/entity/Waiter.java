package com.ktv.entity;

public class Waiter {
    private String waiterId;
    private String name;
    private String gender;
    private String phone;

    public Waiter() {}

    public Waiter(String waiterId, String name, String gender, String phone) {
        this.waiterId = waiterId;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
    }

    public String getWaiterId() { return waiterId; }
    public void setWaiterId(String waiterId) { this.waiterId = waiterId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return "Waiter{" +
                "waiterId='" + waiterId + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}