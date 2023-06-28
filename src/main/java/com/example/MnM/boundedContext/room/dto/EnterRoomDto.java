package com.example.MnM.boundedContext.room.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class EnterRoomDto {
    private String username;
    private Long userId;
}
