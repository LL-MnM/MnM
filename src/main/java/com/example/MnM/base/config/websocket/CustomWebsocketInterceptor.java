package com.example.MnM.base.config.websocket;

import com.example.MnM.boundedContext.room.entity.RoomStatus;
import com.example.MnM.boundedContext.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import static org.springframework.messaging.simp.stomp.StompCommand.CONNECT;
import static org.springframework.messaging.simp.stomp.StompCommand.SUBSCRIBE;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomWebsocketInterceptor implements ChannelInterceptor {

    private final RoomService roomService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        if (CONNECT.equals(headerAccessor.getCommand()) || SUBSCRIBE.equals(headerAccessor.getCommand())) {
            isValid(headerAccessor);
        }
        return message;
    }

    private void isValid(StompHeaderAccessor headerAccessor) {
        String roomStatus = headerAccessor.getFirstNativeHeader("roomStatus");

        if (roomStatus.equals(RoomStatus.SINGLE.name())) {
            String userId = headerAccessor.getFirstNativeHeader("userId");
            roomService.checkSingleRoom(userId);
        } else {
            String roomId = headerAccessor.getFirstNativeHeader("roomId");
            roomService.checkGroupRoom(roomId);
        }
    }

}
