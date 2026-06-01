package com.ktv.entity;

import java.time.LocalDateTime;

public class Reservation {
    private String account;
    private String roomId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Reservation() {}

    public Reservation(String account, String roomId, LocalDateTime startTime, LocalDateTime endTime) {
        this.account = account;
        this.roomId = roomId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    @Override
    public String toString() {
        return "Reservation{" +
                "account='" + account + '\'' +
                ", roomId='" + roomId + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}