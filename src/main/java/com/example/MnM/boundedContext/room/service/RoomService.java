package com.example.MnM.boundedContext.room.service;

import com.example.MnM.base.exception.NotFoundRoomException;
import com.example.MnM.base.exception.NotRoomParticipants;
import com.example.MnM.base.exception.OverCapacityRoomException;
import com.example.MnM.boundedContext.room.entity.ChatRoom;
import com.example.MnM.boundedContext.room.entity.RoomStatus;
import com.example.MnM.boundedContext.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.example.MnM.boundedContext.room.entity.RedisRoom.COUNT;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RoomService {

    private static final Long MAX_CAPACITY = 10L;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RoomRepository roomRepository;

    @Transactional
    public String createRoom(String username, RoomStatus status) {
        String roomSecretId = UUID.randomUUID().toString();

        ChatRoom room = roomRepository.save(ChatRoom.builder()
                .secretId(roomSecretId)
                .createUserName(username)
                .status(status)
                .roomName("%s의 방".formatted(username))
                .build());

        return room.getSecretId();
    }

    @Transactional
    public void deleteDbRoom(String roomSecretId) {
        roomRepository.deleteBySecretId(roomSecretId);
    }

    @Transactional
    public void deleteCacheRoom(String roomSecretId) {
        redisTemplate.delete(COUNT.getKey(roomSecretId));
    }

    @Transactional
    public Long enterRoom(String roomSecretId, String senderName) {
        isExistRoom(roomSecretId);
        return redisTemplate.opsForSet().add(COUNT.getKey(roomSecretId), senderName);
    }

    @Transactional
    public Long exitRoom(String roomSecretId, String senderName) {
        isExistRoom(roomSecretId);
        return redisTemplate.opsForSet().remove(COUNT.getKey(roomSecretId), senderName);
    }

    private void isExistRoom(String roomSecretId) {
        Boolean isExist = redisTemplate.hasKey(COUNT.getKey(roomSecretId));
        if (isExist != null && !isExist)
            throw new NotFoundRoomException("생성되지 않은 방입니다.");
    }


    public ChatRoom findBySecretId(String roomId) {
        return roomRepository.findBySecretId(roomId)
                .orElseThrow(() -> new NotFoundRoomException("생성되지 않은 방입니다."));
    }

    public boolean isRoomOwner(String roomId, String senderName) {
        ChatRoom chatRoom = findBySecretId(roomId);
        return chatRoom.getCreateUserName().equals(senderName);
    }

    public void checkSingleRoom(String roomSecretId, String userId) {

        //TODO Single check
    }

    public void checkGroupRoom(String roomSecretId) {

        Long people = redisTemplate.opsForSet().size(COUNT.getKey(roomSecretId));

        if (people > MAX_CAPACITY)
            throw new OverCapacityRoomException("정원 초과");

    }

    public void isRoomMember(String roomSecretId, String senderName) {
        if (Boolean.FALSE.equals(redisTemplate.opsForSet().isMember(COUNT.getKey(roomSecretId), senderName)))
            throw new NotRoomParticipants("이 방의 참여자가 아닙니다.");
    }

    public ChatRoom findById(Long id) {
        return roomRepository.findById(id).orElseThrow();
    }

    public List<ChatRoom> findAll() {
        return roomRepository.findAll();
    }
}
