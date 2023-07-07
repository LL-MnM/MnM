package com.example.MnM.boundedContext.member.controller;

import com.example.MnM.base.rq.Rq;
import com.example.MnM.base.rsData.RsData;
import com.example.MnM.boundedContext.member.dto.MemberDto;
import com.example.MnM.boundedContext.member.dto.MemberProfileDto;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.service.MemberService;
import com.example.MnM.boundedContext.recommend.service.MemberMbtiService;
import jakarta.validation.Valid;
import org.springframework.context.ApplicationEventPublisher;
import lombok.RequiredArgsConstructor;
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
    private final MemberMbtiService memberMbtiService;
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
        if(bindingResult.hasErrors()){
            return rq.redirectWithMsg("/member/join", "회원가입 실패, 입력하신 정보를 다시 확인해주세요.");
        }
        RsData<Member> joinRs = memberService.join(memberDto);
        if (joinRs.isFail()) {
            return rq.redirectWithMsg("/member/join", joinRs.getMsg());
        }
        return rq.redirectWithMsg("/member/login", joinRs.getMsg());
    }


    @GetMapping("/me")
    public String showMe(Model model) {
        Optional<Member> member = memberService.findByUserName(rq.getMember().getUsername());
        if(member.isEmpty()){return rq.redirectWithMsg("member/login", "회원이 없습니다");}
        model.addAttribute("member", member.get());
        return "member/me";
    }



    @GetMapping("/delete")
    public String MemberDelete() {
        Optional<Member> member = memberService.findByUserName(rq.getMember().getUsername());

        memberService.delete(member.get());
        //Todo : 쿠키 삭제
        return rq.redirectWithMsg("/", "회원이 탈퇴되었습니다.");
    }


    @GetMapping("/editMyPage")
    public String showEditMyPage(Model model) {
        Member member = rq.getMember();
        model.addAttribute("member", member);
        return "member/editMyPage";
    }


    @PostMapping("/editMyPage")
    public String editMyPage(@Valid MemberDto memberDto, BindingResult bindingResult) {
        Optional<Member> member = memberService.findByUserName(rq.getMember().getUsername());

        memberService.modify(member.get(), memberDto);
        //Todo : 사용자 정보 갱신
        //Todo : 쿠키 삭제 or 업데이트
        return rq.redirectWithMsg("/member/me", "회원 정보를 수정하였습니다.");
    }


    @GetMapping("/editProfile")
    public String showEditProfile(Model model) {
        Member member = rq.getMember();
        model.addAttribute("member", member);
        return "member/editProfile";
    }


    @PostMapping("/editProfile")
    public String showEditProfile(@Valid MemberProfileDto memberProfileDto, BindingResult bindingResult) {
        Optional<Member> member = memberService.findByUserName(rq.getMember().getUsername());

        RsData<Member> memberRsData= memberService.modifyProfile(member.get(), memberProfileDto);
        //Todo : 사용자 정보 갱신
        //Todo : 쿠키 삭제 or 업데이트
        return rq.redirectWithMsg("/member/me", memberRsData);
    }

}
