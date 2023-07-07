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

    @Transactional
    public RsData<Notification> makeNotification(Member member, Long id) {

        Optional<Member> toMember = memberRepository.findById(id);
        Notification notification = Notification
                .builder()
                .fromMember(member)
                .toMember(toMember.get())
                .build();

        notificationRepository.save(notification);

        return RsData.of("S-1", "알림 메세지가 생성되었습니다.", notification);
    }
}
