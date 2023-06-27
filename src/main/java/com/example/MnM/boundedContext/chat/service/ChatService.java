package com.example.MnM.boundedContext.chat.service;

import com.example.MnM.boundedContext.chat.dto.ChatMessageDto;
import com.example.MnM.boundedContext.chat.entity.ChatMessage;
import com.example.MnM.boundedContext.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Transactional
@Service
public class ChatService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatRepository chatRepository;

    public void saveChatToCache(String roomId, ChatMessageDto messageDto) {
        redisTemplate.opsForList().rightPush(roomId, messageDto);
        redisTemplate.expire(roomId, 3, TimeUnit.DAYS);
    }

    public void saveChatToDb(String roomSecretId, String roomId) {
        List<Object> list = redisTemplate.opsForList().range(roomSecretId, 0, -1);

        List<ChatMessage> entities = new ArrayList<>();

        for (Object dto : list) {
            ChatMessageDto messageDto = (ChatMessageDto) dto;
            ChatMessage message = ChatMessage.builder()
                    .message(messageDto.getMessage())
                    .writer(messageDto.getSender())
                    .writerId(messageDto.getSenderId())
                    .roomId(roomId)
                    .build();
            entities.add(message);
        }

        chatRepository.saveAll(entities);

    }


    public void deleteCacheChat(String roomId) {
        redisTemplate.delete(roomId);
    }
}
