package com.example.MnM.boundedContext.chat.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class DeleteRoomDto {

    private String roomId;
    private String username;

}
