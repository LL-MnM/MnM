package com.example.MnM.boundedContext.room.repository;

import com.example.MnM.boundedContext.room.entity.ChatRoom;

import com.example.MnM.boundedContext.room.entity.RoomStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<ChatRoom, Long> {

    void deleteBySecretId(String roomId);

    Optional<ChatRoom> findBySecretId(String roomId);

    Page<ChatRoom> findAllByStatus(RoomStatus status, Pageable pageable);
}
