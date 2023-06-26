package com.example.MnM.boundedContext.chat.event;

import com.example.MnM.base.exception.NotOwnerRoomException;
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
    public void deleteRoom(DeleteRoomDto dto) {
        String roomId = dto.getRoomId();

        Long userId = dto.getUserId();

        if (roomService.isRoomOwner(roomId, userId)) {
            roomService.deleteRoom(roomId);
        } else {
            throw new NotOwnerRoomException("방 삭제 권한이 없습니다.");
        }
    }
}
