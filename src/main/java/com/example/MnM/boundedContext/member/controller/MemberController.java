package com.example.MnM.boundedContext.member.controller;

import com.example.MnM.base.rq.Rq;
import com.example.MnM.base.rsData.RsData;
import com.example.MnM.boundedContext.member.dto.MemberDto;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final Rq rq;
    private final ApplicationEventPublisher publisher;


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
        Member member = memberService.findByUserName(rq.getMember().getUsername());
        memberService.delete(member);
        return rq.redirectWithMsg("member/login", "회원이 탈퇴되었습니다.");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/editMyPage")
    public String showEditMyPage(Model model) {
        Member member = memberService.findByUserName(rq.getMember().getUsername());
        model.addAttribute("member", member);
        return "member/editMyPage";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/editMyPage")
    public String editMyPage(@Valid MemberDto memberDto) {
        Member member = memberService.findByUserName(rq.getMember().getUsername());

        memberService.modify(member, memberDto);
        return rq.redirectWithMsg("/member/me", "회원 정보를 수정하였습니다.");
    }
}
