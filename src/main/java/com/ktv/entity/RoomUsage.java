package com.ktv.entity;

import java.time.LocalDateTime;

public class RoomUsage {
    private String account;
    private String roomId;
    private LocalDateTime startTime;   // 改成 LocalDateTime
    private LocalDateTime endTime;     // 改成 LocalDateTime

    /* getter / setter */
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }

    public String getRoomId() {
        return roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}