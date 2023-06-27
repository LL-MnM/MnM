package com.example.MnM.boundedContext.chat.event;

import com.example.MnM.boundedContext.chat.dto.DeleteRoomDto;
import com.example.MnM.boundedContext.chat.service.RoomService;
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
