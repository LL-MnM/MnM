package com.example.MnM.boundedContext.chat.service;

import com.example.MnM.base.exception.NotFoundRoomException;
import com.example.MnM.boundedContext.chat.entity.ChatRoom;
import com.example.MnM.boundedContext.chat.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional
@Service
public class RoomService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RoomRepository roomRepository;

    public List<ChatRoom> findAll() {
        return roomRepository.findAll();
    }

    public String createRoom(Long memberId, String username) {
        String roomSecretId = UUID.randomUUID().toString();


        ChatRoom room = roomRepository.save(ChatRoom.builder()
                .secretId(roomSecretId)
                .createUser(username)
                .createUserId(memberId)
                .name("%s의 방".formatted(username))
                .build());

        Long roomId = room.getId();
        redisTemplate.opsForList().rightPush("rooms", roomId);

        return room.getSecretId();
    }

    public void deleteRoom(String roomId) {
        roomRepository.deleteBySecretId(roomId);
        redisTemplate.opsForList().remove("rooms",1,roomId);
    }

    public ChatRoom findById(Long id) {
        return roomRepository.findById(id).orElseThrow();
    }

    public ChatRoom findBySecretId(String roomId) {
        return roomRepository.findBySecretId(roomId)
                .orElseThrow(() -> new NotFoundRoomException("생성되지 않은 방입니다."));
    }

    public boolean isRoomOwner(String roomId, Long userId) {
        ChatRoom chatRoom = findBySecretId(roomId);
        return chatRoom.getCreateUserId().equals(userId);
    }
}
