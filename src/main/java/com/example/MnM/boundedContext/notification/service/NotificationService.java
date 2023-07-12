package com.example.MnM.boundedContext.notification.service;

import com.example.MnM.base.rsData.RsData;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.repository.MemberRepository;
import com.example.MnM.boundedContext.notification.entity.Notification;
import com.example.MnM.boundedContext.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    public List<Notification> findByToMember(Member member) {
        return notificationRepository.findByToMember(member);
    }

    public List<Notification> findByToMember_username(String username) {
        return notificationRepository.findByToMember_username(username);
    }

    public boolean countUnreadNotificationsByToMember(Member member) {
        return notificationRepository.countByToMemberAndReadDateIsNull(member) > 0;
    }

    @Transactional
    public RsData<Notification> makeNotificationAndCreateRoom(Member member, Long id, String url) {

        Optional<Member> toMember = memberRepository.findById(id);
        Notification notification = Notification
                .builder()
                .fromMember(member)
                .toMember(toMember.get())
                .url(url)
                .build();

        notificationRepository.save(notification);

        return RsData.of("S-1", "알림 메세지가 생성되었습니다.", notification);
    }

    @Transactional
    public RsData markAsRead(List<Notification> notifications) {
        notifications
                .stream()
                .filter(notification -> !notification.isRead())
                .forEach(Notification::markAsRead);

        return RsData.of("S-1", "읽음 처리 되었습니다.");
    }
}
