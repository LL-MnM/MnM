package com.example.MnM.boundedContext.room.dto;

import com.example.MnM.boundedContext.room.entity.RoomStatus;
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
