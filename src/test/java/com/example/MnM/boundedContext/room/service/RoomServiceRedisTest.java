package com.example.MnM.boundedContext.room.service;

import com.example.MnM.base.exception.room.OverCapacityRoomException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import static com.example.MnM.boundedContext.room.entity.RedisRoom.MEMBERS;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class RoomServiceRedisTest {

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Autowired
    RoomService roomService;


    @AfterEach
    void afterTest() {
        redisTemplate.delete("*");
    }

    @DisplayName("single check test 성공")
    @Test
    void checkSingleRoomParticipants() {
        String roomSecretId = "roomSecretId";
        String senderName1 = "user3";
        String senderName2 = "user4";

        redisTemplate.opsForSet().add(MEMBERS.getKey(roomSecretId), senderName1);
        redisTemplate.opsForSet().add(MEMBERS.getKey(roomSecretId), senderName2);

        assertThatCode(() -> {
            roomService.checkSingleRoomParticipants(roomSecretId, senderName1);
            roomService.checkSingleRoomParticipants(roomSecretId, senderName2);
                }).doesNotThrowAnyException();
    }

    @DisplayName("group check test 성공")
    @Test
    void checkGroupRoomTest() {
        String roomSecretId = "roomSecretId";
        String senderName = "user3";

        redisTemplate.opsForSet().add(MEMBERS.getKey(roomSecretId), senderName);

        assertThatCode(() ->
                        roomService.checkGroupRoom(roomSecretId))
                .doesNotThrowAnyException();
    }

    @DisplayName("group 인원 초과")
    @Test
    void checkGroupRoomTestFail() {
        String roomSecretId = "roomSecretId";
        String senderName = "user";

        for (int i = 0; i < 20; i++) {
            redisTemplate.opsForSet().add(MEMBERS.getKey(roomSecretId), senderName+i);
        }

        assertThatThrownBy(() ->
            roomService.checkGroupRoom(roomSecretId)
        ).isInstanceOf(OverCapacityRoomException.class);


    }


}