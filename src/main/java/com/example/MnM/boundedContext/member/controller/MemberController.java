package com.example.MnM.boundedContext.member.controller;

import com.example.MnM.boundedContext.member.service.MemberService;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usr/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @AllArgsConstructor
    @Getter
    @ToString
    public static class JoinForm {
        @NotBlank
        private final String id;
        @NotBlank
        private final String username;
        @NotBlank
        private final String password;
        @NotBlank
        private final String email;
        @NotBlank
        private final String nickname;
        @NotBlank
        private final String interest1;
        @NotBlank
        private final String interest2;
        private final String profileImage;
    }


}
