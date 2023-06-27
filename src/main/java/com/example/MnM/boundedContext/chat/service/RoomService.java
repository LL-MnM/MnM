package com.example.MnM.boundedContext.chat.service;

import com.example.MnM.base.exception.NotFoundRoomException;
import com.example.MnM.boundedContext.chat.dto.SaveChatDto;
import com.example.MnM.boundedContext.chat.entity.ChatRoom;
import com.example.MnM.boundedContext.chat.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Transactional
@Service
public class RoomService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RoomRepository roomRepository;
    private final ApplicationEventPublisher publisher;

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

    public void deleteRoom(String roomSecretId) {
        ChatRoom room = roomRepository.findBySecretId(roomSecretId)
                .orElseThrow(() -> new NotFoundRoomException("방을 찾을 수 없습니다."));
        Long roomId = room.getId();
        roomRepository.delete(room);
        redisTemplate.opsForList().remove("rooms",1,roomSecretId);
        publisher.publishEvent(new SaveChatDto(String.valueOf(roomId),roomSecretId));

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
