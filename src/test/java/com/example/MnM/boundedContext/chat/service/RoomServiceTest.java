package com.example.MnM.boundedContext.chat.service;

import com.example.MnM.base.exception.room.NotFoundRoomException;
import com.example.MnM.base.exception.room.NotRoomParticipants;
import com.example.MnM.boundedContext.room.entity.ChatRoom;
import com.example.MnM.boundedContext.room.entity.RoomStatus;
import com.example.MnM.boundedContext.room.repository.RoomRepository;
import com.example.MnM.boundedContext.room.service.RoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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

    @DisplayName("방 생성 검증")
    @Test
    void creatRoom() {

        String secretRoomId = roomService.createGroupRoom("user3");

        Optional<ChatRoom> bySecretId = roomRepository.findBySecretId(secretRoomId);

        assertThat(bySecretId).isNotEmpty();
        ChatRoom chatRoom = bySecretId.get();
        assertThat(chatRoom.getCreateUserName()).isEqualTo("user3");
        assertThat(chatRoom.getStatus()).isEqualTo(RoomStatus.GROUP);
    }

    @DisplayName("방 주인 검증 성공")
    @Test
    void isRoomOwner() {

        String createUserName = "userName";
        String uniqueId = "uniqueId";

        ChatRoom checkRoom = ChatRoom.builder()
                .createUserName(createUserName)
                .secretId(uniqueId)
                .build();

        roomRepository.save(checkRoom);

        assertThat(roomService.isRoomOwner(uniqueId, createUserName)).isTrue();
    }

    @DisplayName("방 주인 검증 실패 - 존재하지 않는 방")
    @Test
    void notExistRoom() {

        assertThatThrownBy(() -> {
            roomService.isRoomOwner("no", "FakeOwner");
        }).isInstanceOf(NotFoundRoomException.class);
    }

    @DisplayName("방 주인 검증 실패 - 권한 없는 유저")
    @Test
    void hasNotOwner() {

        String createUserName = "userName";
        String uniqueId = "uniqueId";

        ChatRoom checkRoom = ChatRoom.builder()
                .createUserName(createUserName)
                .secretId(uniqueId)
                .build();

        roomRepository.save(checkRoom);

        assertThat(roomService.isRoomOwner(uniqueId, "FakeUser")).isFalse();

    }

    @DisplayName("single room 입장 실패")
    @Test
    void failToEnterSingleRoom() {

        String secretRoomId = roomService.createSingleRoom("user4", "user5");

        assertThatThrownBy(() -> {
            roomService.checkSingleRoomParticipants(secretRoomId, "user3");
        }).isInstanceOf(NotRoomParticipants.class);
    }

}