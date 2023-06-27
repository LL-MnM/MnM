package com.example.MnM.boundedContext.chat.dto;

import com.example.MnM.boundedContext.chat.entity.RoomStatus;
import lombok.*;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class ChatRoomDto {

    private String roomId;
    private String roomName;
    private Long userId;
    private RoomStatus status;

}
