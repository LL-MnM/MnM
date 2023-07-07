package com.example.MnM.boundedContext.notification.repository;

import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByToMember(Member toMember);

}
