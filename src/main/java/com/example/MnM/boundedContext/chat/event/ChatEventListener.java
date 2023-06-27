package com.example.MnM.boundedContext.chat.event;

import com.example.MnM.boundedContext.chat.dto.SaveChatDto;
import com.example.MnM.boundedContext.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.SessionConnectedEvent;


@RequiredArgsConstructor
@Component
public class ChatEventListener {

    private final ChatService chatService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @EventListener(SaveChatDto.class)
    public void saveChatToDb(SaveChatDto saveChatDto) {
        String roomId = saveChatDto.getRoomId();
        String roomSecretId = saveChatDto.getRoomSecretId();
        chatService.saveChatToDb(roomSecretId,roomId);
        chatService.deleteCacheChat(roomSecretId);
    }
}
