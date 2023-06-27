package com.example.MnM.boundedContext.chat.service;

import com.example.MnM.base.exception.NotFoundRoomException;
import com.example.MnM.boundedContext.chat.entity.ChatRoom;
import com.example.MnM.boundedContext.chat.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class RoomServiceTest {

    @Autowired
    RoomService roomService;

    @Autowired
    RoomRepository roomRepository;

    @DisplayName("방 주인 검증 성공")
    @Test
    void isRoomOwner() {

        Long userId = 1L;
        String uniqueId = "uniqueId";

        ChatRoom checkRoom = ChatRoom.builder()
                .createUserId(userId)
                .secretId(uniqueId)
                .build();

        roomRepository.save(checkRoom);

        assertThat(roomService.isRoomOwner(uniqueId,userId)).isTrue();
    }

    @DisplayName("방 주인 검증 실패 - 존재하지 않는 방")
    @Test
    void notExistRoom() {

        assertThatThrownBy(() -> {
            roomService.isRoomOwner("no",20L);
        }).isInstanceOf(NotFoundRoomException.class);
    }

    @DisplayName("방 주인 검증 실패 - 권한 없는 유저")
    @Test
    void hasNotOwner() {

        Long userId = 30L;
        String uniqueId = "uniqueId";

        ChatRoom checkRoom = ChatRoom.builder()
                .createUserId(userId)
                .secretId(uniqueId)
                .build();

        roomRepository.save(checkRoom);

        assertThat(roomService.isRoomOwner(uniqueId,userId+1)).isFalse();

    }

}