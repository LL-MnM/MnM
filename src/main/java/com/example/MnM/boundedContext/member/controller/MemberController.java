package com.example.MnM.boundedContext.member.controller;

import com.example.MnM.base.rsData.RsData;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.service.MemberService;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @AllArgsConstructor
    @Getter
    @ToString
    public static class JoinForm { //임시
        @NotBlank
        private final String userId; //id
        @NotBlank
        private final String password;
        @NotBlank
        private final String email;
        @NotBlank
        private final String username; //이름
        @NotBlank
        private final String nickname; //닉네임
        @NotBlank
        private final Integer height; //키
        @NotBlank
        private final Integer age; //나이;
        @NotBlank
        private final String locate; //지역
        @NotBlank
        private final String gender; //성별
        @NotBlank
        private final String mbti; //mbti
        @NotBlank
        private final String hobby; //취미
        @NotBlank
        private final String introduce; //자기소개
        private final String profileImage;
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin() {
        return "member/login";
    }

    /*@PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin() {
        return "member/join";
    }

    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm) {
        RsData<Member> joinRs = memberService.join(joinForm);
        if (joinRs.isFail()) {
            return "redirect:/member/join";
        }
        return "redirect:/member/login";
    }*/


}
