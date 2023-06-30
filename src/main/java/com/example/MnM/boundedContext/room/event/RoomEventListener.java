package com.example.MnM.boundedContext.room.event;

import com.example.MnM.boundedContext.room.dto.DeleteRoomDto;
import com.example.MnM.boundedContext.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RoomEventListener {

    private final RoomService roomService;

    @EventListener(DeleteRoomDto.class)
    public void deleteRoomEvent(DeleteRoomDto dto) {
        String roomId = dto.getRoomId();
        roomService.deleteRoom(roomId);
    }
}
