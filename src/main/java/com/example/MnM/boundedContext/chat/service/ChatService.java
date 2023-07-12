package com.example.MnM.boundedContext.chat.service;

import com.example.MnM.boundedContext.chat.dto.ChatMessageDto;
import com.example.MnM.boundedContext.chat.entity.ChatMessage;
import com.example.MnM.boundedContext.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.MnM.boundedContext.chat.entity.RedisChat.CHAT;
import static com.example.MnM.boundedContext.room.entity.RedisRoom.*;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ChatService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatRepository chatRepository;
    private final FacadeChatService facadeChatService;

    public void saveChatToCache(String roomSecretId, ChatMessageDto messageDto) {
        redisTemplate.opsForList().rightPush(CHAT.getKey(roomSecretId), messageDto);
        redisTemplate.expire(roomSecretId, 3, TimeUnit.DAYS);
    }

    public void saveChatToDb(String roomSecretId) {

        List<Object> list = redisTemplate.opsForList().range(CHAT.getKey(roomSecretId), 0, -1);

        if (list.size() <= 5)
            return;

        StringBuilder sb = new StringBuilder();

        List<ChatMessage> entities = new ArrayList<>();

        for (Object dto : list) {
            ChatMessageDto messageDto = (ChatMessageDto) dto;
            String message = messageDto.getMessage();
            String senderName = messageDto.getSenderName();

            ChatMessage chatMessage = ChatMessage.builder()
                    .message(message)
                    .writer(senderName)
                    .roomId(roomSecretId)
                    .build();
            entities.add(chatMessage);

            sb.append(senderName).append(": ").append(message).append("\n");
        }
        facadeChatService.inspectChat(roomSecretId, sb.toString());

        chatRepository.saveAll(entities);

    }


    public void deleteCacheChat(String roomId) {
        redisTemplate.delete(CHAT.getKey(roomId));
    }
}
