package com.example.MnM.boundedContext.room.service;

import com.example.MnM.base.exception.NotFoundRoomException;
import com.example.MnM.base.exception.OverCapacityRoomException;
import com.example.MnM.boundedContext.chat.dto.SaveChatDto;
import com.example.MnM.boundedContext.room.entity.ChatRoom;
import com.example.MnM.boundedContext.room.entity.RoomStatus;
import com.example.MnM.boundedContext.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RoomService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RoomRepository roomRepository;
    private final ApplicationEventPublisher publisher;

    private final Long MAX_CAPACITY = 10L;

    @Transactional
    public String createRoom(Long memberId, String username, RoomStatus status) {
        String roomSecretId = UUID.randomUUID().toString();


        ChatRoom room = roomRepository.save(ChatRoom.builder()
                .secretId(roomSecretId)
                .createUser(username)
                .createUserId(memberId)
                .status(status)
                .name("%s의 방".formatted(username))
                .build());

        redisTemplate.opsForHash().put("rooms", room.getSecretId(), 0L);

        return room.getSecretId();
    }

    @Transactional
    public void deleteRoom(String roomSecretId) {
        ChatRoom room = roomRepository.findBySecretId(roomSecretId)
                .orElseThrow(() -> new NotFoundRoomException("방을 찾을 수 없습니다."));

        Long roomId = room.getId();
        roomRepository.delete(room);

        redisTemplate.opsForHash().delete("rooms", roomId);

        publisher.publishEvent(new SaveChatDto(String.valueOf(roomId), roomSecretId));
    }

    @Transactional
    public Long enterRoom(String roomSecretId) {
        isExistRoom(roomSecretId);
        return redisTemplate.opsForHash().increment("rooms", roomSecretId, 1L);
    }

    @Transactional
    public Long exitRoom(String roomSecretId) {
        isExistRoom(roomSecretId);
        return redisTemplate.opsForHash().increment("rooms", roomSecretId, -1L);
    }

    private void isExistRoom(String roomSecretId) {
        boolean isExist = redisTemplate.opsForHash().hasKey("rooms", roomSecretId);
        if (!isExist)
            throw new NotFoundRoomException("생성되지 않은 방입니다.");
    }


    public ChatRoom findBySecretId(String roomId) {
        return roomRepository.findBySecretId(roomId)
                .orElseThrow(() -> new NotFoundRoomException("생성되지 않은 방입니다."));
    }

    public ChatRoom findById(Long id) {
        return roomRepository.findById(id).orElseThrow();
    }

    public List<ChatRoom> findAll() {
        return roomRepository.findAll();
    }

    public boolean isRoomOwner(String roomId, Long userId) {
        ChatRoom chatRoom = findBySecretId(roomId);
        return chatRoom.getCreateUserId().equals(userId);
    }

    public void checkSingleRoom(String roomId, String userId) {

        //TODO Single check
    }

    public void checkGroupRoom(String roomId) {

        Integer people = (Integer) redisTemplate.opsForHash().get("rooms", roomId);

        if (people >= MAX_CAPACITY)
            throw new OverCapacityRoomException("정원 초과");

    }
}
