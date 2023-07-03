package com.example.MnM.boundedContext.chat.event;

import com.example.MnM.boundedContext.chat.dto.SaveChatDto;
import com.example.MnM.boundedContext.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Component
public class ChatEventListener {

    private final ChatService chatService;

    @Async("messageThreadPool")
    @Transactional
    @EventListener(SaveChatDto.class)
    public void saveChatToDb(SaveChatDto saveChatDto) {

        String roomSecretId = saveChatDto.getRoomSecretId();

        chatService.saveChatToDb(roomSecretId);
        chatService.deleteCacheChat(roomSecretId);
    }
}
