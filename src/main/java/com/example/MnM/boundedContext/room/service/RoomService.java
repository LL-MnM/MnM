package com.example.MnM.boundedContext.room.service;

import com.example.MnM.base.exception.NotFoundRoomException;
import com.example.MnM.base.exception.NotRoomParticipants;
import com.example.MnM.base.exception.OverCapacityRoomException;
import com.example.MnM.boundedContext.room.entity.ChatRoom;
import com.example.MnM.boundedContext.room.entity.RoomStatus;
import com.example.MnM.boundedContext.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.example.MnM.boundedContext.room.entity.RedisRoom.MEMBERS;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RoomService {

    private static final Long MAX_CAPACITY = 10L;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RoomRepository roomRepository;

    @Transactional
    public String createGroupRoom(String username) {
        ChatRoom room = createRoom(username, RoomStatus.GROUP);

        return room.getSecretId();
    }

    @Transactional
    public String createSingleRoom(String inviter,String invitee ) {
        ChatRoom room = createRoom(inviter, RoomStatus.SINGLE);

        redisTemplate.opsForSet().add(MEMBERS.getKey(room.getSecretId()),inviter);
        redisTemplate.opsForSet().add(MEMBERS.getKey(room.getSecretId()),invitee);

        return room.getSecretId();
    }

    @Transactional
    public ChatRoom createRoom(String username, RoomStatus status) {
        String roomSecretId = UUID.randomUUID().toString();

        return roomRepository.save(ChatRoom.builder()
                .secretId(roomSecretId)
                .createUserName(username)
                .status(status)
                .roomName("%s의 방".formatted(username))
                .build());
    }

    @Transactional
    public void deleteDbRoom(String roomSecretId) {
        roomRepository.deleteBySecretId(roomSecretId);
    }

    @Transactional
    public void deleteCacheRoom(String roomSecretId) {
        redisTemplate.delete(MEMBERS.getKey(roomSecretId));
    }

    @Transactional
    public void enterRoom(String roomSecretId, String senderName) {

        ChatRoom chatRoom =
                roomRepository.findBySecretId(roomSecretId).orElseThrow(() -> new NotFoundRoomException("없는 방입니다"));

        if (chatRoom.isGroup()) {
            redisTemplate.opsForSet().add(MEMBERS.getKey(roomSecretId), senderName);
        }
    }

    @Transactional
    public Long exitRoom(String roomSecretId, String senderName) {
        isExistRoom(roomSecretId);
        return redisTemplate.opsForSet().remove(MEMBERS.getKey(roomSecretId), senderName);
    }

    private void isExistRoom(String roomSecretId) {
        Boolean isExist = redisTemplate.hasKey(MEMBERS.getKey(roomSecretId));
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

    public ChatRoom checkValidate(String roomId, String username) {
        ChatRoom room = findBySecretId(roomId);

        if (!room.isGroup()) {
            checkSingleRoomParticipants(roomId,username);
        }
        checkGroupRoom(room.getSecretId());
        return room;
    }

    public void checkGroupRoom(String roomSecretId) {

        Long people = redisTemplate.opsForSet().size(MEMBERS.getKey(roomSecretId));

        if (people >= MAX_CAPACITY)
            throw new OverCapacityRoomException("정원 초과");

    }

    public void checkSingleRoomParticipants(String roomSecretId, String senderName) {
        if (Boolean.FALSE.equals(redisTemplate.opsForSet().isMember(MEMBERS.getKey(roomSecretId), senderName)))
            throw new NotRoomParticipants("이 방의 참여자가 아닙니다.");
    }

    public Page<ChatRoom> getList(int page) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        return roomRepository.findAllByStatus(RoomStatus.GROUP,pageable);
    }

    public ChatRoom findById(Long id) {
        return roomRepository.findById(id).orElseThrow();
    }
}
