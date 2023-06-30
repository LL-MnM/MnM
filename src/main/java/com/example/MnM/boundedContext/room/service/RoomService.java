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

    private final RedisTemplate<String, Object> redisTemplate;
    private final RoomRepository roomRepository;

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

        return room.getSecretId();
    }

    @Transactional
    public void deleteRoom(String roomSecretId) {
        ChatRoom room = roomRepository.findBySecretId(roomSecretId)
                .orElseThrow(() -> new NotFoundRoomException("방을 찾을 수 없습니다."));

        roomRepository.delete(room);
        redisTemplate.delete(COUNT.getKey(roomSecretId));
    }

    @Transactional
    public Long enterRoom(String roomSecretId, String userId) {
        isExistRoom(roomSecretId);
        return redisTemplate.opsForSet().add(COUNT.getKey(roomSecretId), userId);
    }

    @Transactional
    public Long exitRoom(String roomSecretId, String userId) {
        isExistRoom(roomSecretId);
        return redisTemplate.opsForSet().remove(COUNT.getKey(roomSecretId), userId);
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

    public void checkSingleRoom(String roomSecretId, String userId) {

        //TODO Single check
    }

    public void checkGroupRoom(String roomSecretId) {

        Long people = redisTemplate.opsForSet().size(COUNT.getKey(roomSecretId));

        if (people > MAX_CAPACITY)
            throw new OverCapacityRoomException("정원 초과");

    }

    public void isRoomMember(String roomSecretId, Long senderId) {
        if (Boolean.FALSE.equals(redisTemplate.opsForSet().isMember(COUNT.getKey(roomSecretId), String.valueOf(senderId))))
            throw new NotRoomParticipants("이 방의 참여자가 아닙니다.");
    }
}
