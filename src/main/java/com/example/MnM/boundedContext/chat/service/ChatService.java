package com.example.MnM.boundedContext.chat.service;

import com.example.MnM.boundedContext.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void saveChat(String roomId, ChatMessageDto messageDto) {
        redisTemplate.opsForList().rightPush(roomId, messageDto);
    }



}
