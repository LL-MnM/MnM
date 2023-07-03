package com.example.MnM.boundedContext.recommend.controller;

import com.example.MnM.base.rq.Rq;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.recommend.service.MemberMbtiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/recommend")
@RequiredArgsConstructor
public class MbtiController {
    private final MemberMbtiService memberMbtiService;
    private final Rq rq;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/member")
    public String showRecommend(Model model) {
        Member member = rq.getMember();
        List<Member> mbtiForMember = memberMbtiService.findMbtiForMember(member);
        model.addAttribute("mbtiForMember", mbtiForMember);
        return "/recommend/member";
    }

}
