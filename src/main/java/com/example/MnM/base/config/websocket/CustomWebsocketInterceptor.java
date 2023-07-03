package com.example.MnM.base.config.websocket;

import com.example.MnM.boundedContext.room.entity.RoomStatus;
import com.example.MnM.boundedContext.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import static com.example.MnM.boundedContext.room.entity.RoomStatus.*;
import static org.springframework.messaging.simp.stomp.StompCommand.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomWebsocketInterceptor implements ChannelInterceptor {

    private final RoomService roomService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        String roomStatus = headerAccessor.getFirstNativeHeader("roomStatus");
        String roomId = headerAccessor.getFirstNativeHeader("roomId");
        String userId = headerAccessor.getFirstNativeHeader("userId");

        StompCommand command = headerAccessor.getCommand();

        if (command == CONNECT) {
            isValid(roomStatus, roomId, userId);
            roomService.enterRoom(roomId,userId);
        }

        return message;
    }

    private void isValid(String roomStatus, String roomId,String userId ) {

        if (roomStatus.equals(SINGLE.name())) {
            roomService.checkSingleRoom(roomId,userId);
            return;
        }

        roomService.checkGroupRoom(roomId);

    }

}
