package com.example.MnM.base.config.websocket;

import com.example.MnM.base.exception.NotValidRoomException;
import com.example.MnM.boundedContext.chat.entity.ChatRoom;
import com.example.MnM.boundedContext.chat.service.RoomService;
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

        String username = headerAccessor.getFirstNativeHeader("username");
        String roomId = headerAccessor.getFirstNativeHeader("roomId");

        ChatRoom chatRoom = roomService.findBySecretId(roomId);

        if (!isRoomOwner(username, chatRoom))
            throw new NotValidRoomException("권한이 없는 방입니다.");

    }

    private boolean isRoomOwner(String username, ChatRoom chatRoom) {
        return chatRoom.getCreateUser().equals(username);
    }
}
