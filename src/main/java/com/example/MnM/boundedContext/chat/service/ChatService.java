package com.example.MnM.boundedContext.chat.service;

import com.example.MnM.boundedContext.chat.dto.ChatMessageDto;
import com.example.MnM.boundedContext.chat.entity.ChatMessage;
import com.example.MnM.boundedContext.chat.repository.ChatRepository;
import com.example.MnM.boundedContext.room.dto.DeleteRoomDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.MnM.boundedContext.chat.entity.RedisChat.CHAT;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ChatService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatRepository chatRepository;
    private final FacadeChatService facadeChatService;
    private final ApplicationEventPublisher publisher;

    public void saveChatToCache(String roomSecretId, ChatMessageDto messageDto) {
        redisTemplate.opsForList().rightPush(CHAT.getKey(roomSecretId), messageDto);
        redisTemplate.expire(roomSecretId, 3, TimeUnit.DAYS);
    }

    public void saveChatToDb(String roomSecretId ) {

        List<Object> list = redisTemplate.opsForList().range(CHAT.getKey(roomSecretId), 0, -1);

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
                    .roomId(roomSecretId)
                    .build();
            entities.add(chatMessage);

            sb.append(sender).append(": ").append(message).append("\n");
        }
        facadeChatService.inspectChat(roomSecretId,sb.toString());

        redisTemplate.delete(CHAT.getKey(roomSecretId));

        chatRepository.saveAll(entities);

    }


    public void deleteCacheChat(String roomId) {
        redisTemplate.delete(CHAT.getKey(roomId));
        publisher.publishEvent(new DeleteRoomDto(roomId));
    }
}
