package com.example.MnM.boundedContext.chat.repository;

import com.example.MnM.boundedContext.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<ChatRoom, Long> {

    void deleteByUniqueId(String roomId);
    Optional<ChatRoom> findByUniqueId(String roomId);
}
