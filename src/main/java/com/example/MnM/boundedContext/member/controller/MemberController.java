package com.example.MnM.boundedContext.member.controller;

import com.example.MnM.base.rq.Rq;
import com.example.MnM.base.rsData.RsData;
import com.example.MnM.boundedContext.member.dto.MemberDto;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final Rq rq;


    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin() {
        return "member/login";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin() {
        return "member/join";
    }

    @PostMapping("/join")
    public String join(@Valid MemberDto memberDto, BindingResult bindingResult) {
        RsData<Member> joinRs = memberService.join(memberDto);
        if(bindingResult.hasErrors()){
            return rq.redirectWithMsg("/member/join", "입력하신 정보를 다시 확인해주세요.");
        }
        if (joinRs.isFail()) {
            return rq.redirectWithMsg("/member/join", "로그인에 실패하였습니다.");
        }
        return rq.redirectWithMsg("/member/me", joinRs);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public String showMe(Model model) {
        return "member/me";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete")
    public String MemberDelete() {
        RsData deleteRs = memberService.deleteMember(rq.getMember());
        return rq.redirectWithMsg("member/login", deleteRs);
    }
}
