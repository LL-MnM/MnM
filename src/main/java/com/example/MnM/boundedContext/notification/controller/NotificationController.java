package com.example.MnM.boundedContext.notification.controller;

import com.example.MnM.base.rq.Rq;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.repository.MemberRepository;
import com.example.MnM.boundedContext.notification.entity.Notification;
import com.example.MnM.boundedContext.notification.service.NotificationService;
import com.example.MnM.boundedContext.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final Rq rq;
    private final NotificationService notificationService;
    private final MemberRepository memberRepository;
    private final RoomService roomService;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String showList(Model model) {
        Member member = rq.getMember();
        List<Notification> notifications = notificationService.findByToMember(member);

        notificationService.markAsRead(notifications);

        model.addAttribute("notifications", notifications);

        return "notification/list";
    }

    @PostMapping("/alarm/{id}")
    @PreAuthorize("isAuthenticated()")
    public String makeNotificationAndCreateRoom(Model model, @PathVariable Long id) {
        // 방 생성
        String inviter = rq.getMember().getUsername(); // 알림 생성자를 방 초대자로 사용
        String invitee = memberRepository.findById(id).get().getUsername(); // 초대할 사용자를 정의
        String roomId = roomService.createSingleRoom(inviter, invitee);
        String url = "/chat/room/" + roomId;
        model.addAttribute(roomId, "roomId");
        notificationService.makeNotificationAndCreateRoom(rq.getMember(), id, url);
        return rq.redirectWithMsg("/chat/room/" + roomId, "채팅방이 생성되었습니다.");
    }
}