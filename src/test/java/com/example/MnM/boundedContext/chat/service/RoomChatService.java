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
    ChatService chatService;

    @MockBean
    RoomService roomService;


    @DisplayName("채팅 삭제후 방 삭제")
    @Test
    void deleteCacheChatWillDeleteRoom() {
        ChatRoom room = ChatRoom.builder()
                .secretId("secretId")
                .build();
        roomRepository.save(room);

        doNothing().when(roomService).deleteRoom(room.getSecretId());

        chatService.deleteCacheChat("secretId");

        verify(roomService,times(1)).deleteRoom(any());
    }

}
