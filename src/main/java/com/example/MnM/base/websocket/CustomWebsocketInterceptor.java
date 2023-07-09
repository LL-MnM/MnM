package com.example.MnM.base.websocket;

import com.example.MnM.boundedContext.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.example.MnM.boundedContext.room.entity.RoomStatus.SINGLE;
import static org.springframework.messaging.simp.stomp.StompCommand.CONNECT;

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
        String senderName = headerAccessor.getFirstNativeHeader("senderName");

        StompCommand command = headerAccessor.getCommand();

        if (command == CONNECT && isNotEmpty(roomStatus,roomId,senderName)) {
            isValid(roomStatus, roomId, senderName);
            roomService.enterRoom(roomId, senderName);
        }

        return message;
    }

    private boolean isNotEmpty(String roomStatus, String roomId, String senderName) {
        return StringUtils.hasText(roomStatus) && StringUtils.hasText(roomId) && StringUtils.hasText(senderName);
    }

    private void isValid(String roomStatus, String roomId, String senderName) {

        if (roomStatus.equals(SINGLE.name())) {
            roomService.checkSingleRoomParticipants(roomId, senderName);
            return;
        }

        roomService.checkGroupRoom(roomId);
    }

}
