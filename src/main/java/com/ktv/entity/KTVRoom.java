package com.ktv.entity;

import java.util.Date;

public class KTVRoom {
    private String roomId;
    private String roomType;
    private int sales;
    private String roomStatus;


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    private String customerId;
    private String customerName;
    private String customerPhone;
    private Date startDate;
    private Date endDate;
    public KTVRoom() {}

    public KTVRoom(String roomId, String roomType, int sales, String roomStatus) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.sales = sales;
        this.roomStatus = roomStatus;
    }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    public int getSales() { return sales; }
    public void setSales(int sales) { this.sales = sales; }

    public String getRoomStatus() { return roomStatus; }
    public void setRoomStatus(String roomStatus) { this.roomStatus = roomStatus; }

    @Override
    public String toString() {
        return "KTVRoom{" +
                "roomId='" + roomId + '\'' +
                ", roomType='" + roomType + '\'' +
                ", sales=" + sales +
                ", roomStatus='" + roomStatus + '\'' +
                '}';
    }
}