package com.ktv.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EndRoomDTO {
    private String roomNo;
    private LocalDateTime endTime;
}