package com.ktv.entity;

import java.time.LocalDateTime;

public class RoomUsage {
    private String account;      // 顾客账号
    private String roomId;       // 房间号
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;       // 使用中 / 已结束

    // Getters and Setters
    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}