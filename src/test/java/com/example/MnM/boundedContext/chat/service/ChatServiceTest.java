package com.example.MnM.boundedContext.chat.service;

import com.example.MnM.boundedContext.chat.dto.ChatMessageDto;
import com.example.MnM.boundedContext.chat.entity.ChatStatus;
import com.example.MnM.boundedContext.chat.repository.ChatRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static com.example.MnM.boundedContext.chat.entity.RedisChat.CHAT;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class ChatServiceTest {

    @InjectMocks
    ChatService chatService;

    @Mock
    ChatRepository chatRepository;

    @Mock
    RedisTemplate<String,Object> redisTemplate;

    @Mock
    ListOperations<String,Object> listOperations;
    @Mock
    FacadeChatService facadeChatService;


    @DisplayName("채팅 저장 호출 확인 테스트")
    @Test
    void saveChatToDbTest() {

        given(redisTemplate.opsForList()).willReturn(listOperations);
        String roomSecretId = CHAT.getKey("roomSecretId");

        chatService.saveChatToDb("roomSecretId");

        verify(chatRepository,times(1)).saveAll(any());
        verify(redisTemplate,times(1)).delete(roomSecretId);
        verify(facadeChatService,times(1)).inspectChat(any(),any());
    }

    @Test
    void saveChatToCacheTest() {
        String roomSecretId = "roomSecretId";
        ChatMessageDto messageDto =
                new ChatMessageDto("1", "user5", 10L, "message", ChatStatus.SEND);

        given(redisTemplate.opsForList()).willReturn(listOperations);

        chatService.saveChatToCache(roomSecretId, messageDto);

        verify(listOperations, times(1)).rightPush(CHAT.getKey(roomSecretId), messageDto);
        verify(redisTemplate, times(1)).expire(roomSecretId, 3, TimeUnit.DAYS);
    }

}