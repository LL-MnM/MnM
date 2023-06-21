package com.example.MnM.boundedContext.chat.dto;

import lombok.*;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class ChatRoomDto {

    private String roomId;
    private String roomName;
    private Long userId;

}
