package com.example.MnM.boundedContext.chat.service;

import com.example.MnM.boundedContext.room.entity.ChatRoom;
import com.example.MnM.boundedContext.room.repository.RoomRepository;
import com.example.MnM.boundedContext.room.service.RoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class RoomChatService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomService roomService;

    @MockBean
    ChatService mockService;

    @DisplayName("방 삭제시 해당 채팅 로그 영속화")
    @Test
    void saveChatWhenDeleteRoom() {
        ChatRoom room = ChatRoom.builder()
                .secretId("secretId")
                .build();
        roomRepository.save(room);

        doNothing().when(mockService).saveChatToCache(any(), any());
        doNothing().when(mockService).deleteCacheChat(room.getSecretId());

        roomService.deleteRoom(room.getSecretId());

        verify(mockService,times(1)).saveChatToDb(any(),any());
        verify(mockService,times(1)).deleteCacheChat(any());
    }
}
