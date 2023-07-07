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
import org.springframework.data.redis.core.SetOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.MnM.boundedContext.chat.entity.RedisChat.CHAT;
import static com.example.MnM.boundedContext.room.entity.RedisRoom.MEMBERS;
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

    @Mock
    SetOperations<String,Object> setOperations;


    @DisplayName("너무 적은 채팅은 저장 안함")
    @Test
    void notSaveChat() {

        given(redisTemplate.opsForList()).willReturn(listOperations);
        given(redisTemplate.opsForSet()).willReturn(setOperations);

        List<Object> list = new ArrayList<>();

        String roomSecretId = "roomSecretId";
        when(listOperations.range(CHAT.getKey(roomSecretId), 0, -1)).thenReturn(list);
        when(setOperations.size(MEMBERS.getKey(roomSecretId))).thenReturn(1L);

        chatService.saveChatToDb(roomSecretId);

        verify(chatRepository,never()).saveAll(any());
        verify(redisTemplate,never()).delete(CHAT.getKey(roomSecretId));
        verify(facadeChatService,never()).inspectChat(any(),any());
    }

    @DisplayName("채팅 저장 호출 확인 테스트")
    @Test
    void saveChatToDbTest() {

        given(redisTemplate.opsForList()).willReturn(listOperations);
        given(redisTemplate.opsForSet()).willReturn(setOperations);

        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            list.add(new ChatMessageDto("roomId","senderName","message",ChatStatus.SEND));
        }

        String roomSecretId = "roomSecretId";
        when(listOperations.range(CHAT.getKey(roomSecretId), 0, -1)).thenReturn(list);
        when(setOperations.size(MEMBERS.getKey(roomSecretId))).thenReturn(7L);

        chatService.saveChatToDb(roomSecretId);

        verify(chatRepository,times(1)).saveAll(any());
        verify(facadeChatService,times(1)).inspectChat(any(),any());
    }

    @DisplayName("채팅 캐시 저장 성공")
    @Test
    void saveChatToCacheTest() {
        String roomSecretId = "roomSecretId";
        ChatMessageDto messageDto =
                new ChatMessageDto("1", "user5", "message", ChatStatus.SEND);

        given(redisTemplate.opsForList()).willReturn(listOperations);

        chatService.saveChatToCache(roomSecretId, messageDto);

        verify(listOperations, times(1)).rightPush(CHAT.getKey(roomSecretId), messageDto);
        verify(redisTemplate, times(1)).expire(roomSecretId, 3, TimeUnit.DAYS);
    }

}