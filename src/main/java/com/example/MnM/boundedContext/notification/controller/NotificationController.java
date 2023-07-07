package com.example.MnM.boundedContext.notification.controller;

import com.example.MnM.base.rq.Rq;
import com.example.MnM.base.rsData.RsData;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.notification.entity.Notification;
import com.example.MnM.boundedContext.notification.service.NotificationService;
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

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String showList(Model model) {
        Member member = rq.getMember();
        List<Notification> notifications = notificationService.findByToMember(member);

        model.addAttribute("notifications", notifications);

        return "notification/list";
    }

    @PostMapping("/alarm/{id}")
    @PreAuthorize("isAuthenticated()")
    public String makeAlarm(@PathVariable Long id) {
        RsData<Notification> makeNotification = notificationService.makeNotification(rq.getMember(), id);
        return rq.redirectWithMsg("/notification/list", makeNotification);
    }
}