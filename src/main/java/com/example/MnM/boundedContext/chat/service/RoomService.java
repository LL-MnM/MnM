package com.example.MnM.boundedContext.chat.service;

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

    public Long createRoom() {
        String roomSecretId = UUID.randomUUID().toString();

        ChatRoom room = roomRepository.save(ChatRoom.builder()
                .uniqueId(roomSecretId)
                .createUser(roomSecretId)
                .name(roomSecretId)
                .build());

        Long roomId = room.getId();
        redisTemplate.opsForList().rightPush("rooms", roomId);

        return roomId;

    }

    public void deleteRoom(String roomId) {
        roomRepository.deleteByUniqueId(roomId);
        redisTemplate.opsForList().remove("rooms",1,roomId);
    }

    public ChatRoom findById(Long id) {
        return roomRepository.findById(id).orElseThrow();
    }
}
