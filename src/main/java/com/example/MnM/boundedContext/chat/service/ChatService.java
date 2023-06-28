package com.example.MnM.boundedContext.chat.service;

import com.example.MnM.boundedContext.chat.dto.ChatMessageDto;
import com.example.MnM.boundedContext.chat.entity.ChatMessage;
import com.example.MnM.boundedContext.chat.entity.EmotionDegree;
import com.example.MnM.boundedContext.chat.infra.InspectSentimentService;
import com.example.MnM.boundedContext.chat.repository.ChatRepository;
import com.example.MnM.boundedContext.chat.repository.EmotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ChatService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatRepository chatRepository;
    private final EmotionRepository emotionRepository;
    private final InspectSentimentService inspectSentimentService;

    public void saveChatToCache(String roomId, ChatMessageDto messageDto) {
        redisTemplate.opsForList().rightPush(roomId, messageDto);
        redisTemplate.expire(roomId, 3, TimeUnit.DAYS);
    }

    public void saveChatToDb(String roomSecretId, String roomId) {
        List<Object> list = redisTemplate.opsForList().range(roomSecretId, 0, -1);

        StringBuilder sb = new StringBuilder();

        List<ChatMessage> entities = new ArrayList<>();

        for (Object dto : list) {
            ChatMessageDto messageDto = (ChatMessageDto) dto;
            String message = messageDto.getMessage();
            String sender = messageDto.getSender();

            ChatMessage chatMessage = ChatMessage.builder()
                    .message(message)
                    .writer(sender)
                    .writerId(messageDto.getSenderId())
                    .roomId(roomId)
                    .build();
            entities.add(chatMessage);

            sb.append(sender).append(": ").append(message).append("\n");
        }

        try {
            inspectSentimentService.chatInspectSentiment("hello",sb.toString())
                    .thenApplyAsync(emotionRepository::save);
        } catch (IOException e) {
            log.error("inspectSentimentService error",e);
            throw new RuntimeException(e);
        }

        chatRepository.saveAll(entities);

    }


    public void deleteCacheChat(String roomId) {
        redisTemplate.delete(roomId);
    }
}
