package com.example.MnM.boundedContext.member.controller;


import com.example.MnM.boundedContext.board.entity.question.DataNotFoundException;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.repository.MemberRepository;
import com.example.MnM.boundedContext.member.service.MemberService;
import groovy.transform.builder.Builder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class MemberControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;

    Member member;

    @BeforeEach
    public void Test() { //테스트 멤버 작성
        member = Member.builder()
                .username("test001")
                .name("홍길동")
                .password("1234")
                .build();//testcase
    }
/*회원가입*/
    @DisplayName("회원가입 폼")
    void showMemberJoinTest() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/member/join"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("showJoin"))
                .andExpect(content().string(containsString("회원가입")));
    }

        @Test
        @DisplayName("회원가입")
        void MemberJoinTest() throws Exception {
            // WHEN
            ResultActions resultActions = mvc
                    .perform(post("/member/join")
                            .with(csrf())
                            .param("username", "test001")
                            .param("password", "1234")
                            .param("email", "user1@test.com")
                    )
                    .andDo(print());

            // THEN
            resultActions
                    .andExpect(status().is3xxRedirection())
                    .andExpect(handler().handlerType(MemberController.class))
                    .andExpect(handler().methodName("join"))
                    .andExpect(redirectedUrlPattern("/member/login?msg=**"));

            assertThat(memberService.findByUserName("test001").isPresent()).isTrue();
        }


    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("마이페이지")
    void MypageTest() throws Exception {
        ResultActions resultActions = mvc
                .perform(get("/member/me"))
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("showMe"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("회원 탈퇴")
    void deleteMember() throws Exception {
        ResultActions resultActions = mvc
                .perform(get("/member/delete")
                ).andDo(print());

        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("MemberDelete"))
                .andExpect(status().is3xxRedirection());

        assertThat(memberService.findByUserName("user1")).isEmpty();
    }

    @Test
    @DisplayName("soft delete test")
    public void softDeleteTest() {

        memberRepository.save(member);
        assertThat(memberService.findByUserName(member.getUsername())).isNotNull();
        assertThat(member.isDeleted()).isFalse();

        memberService.deleteMember(member);

        Optional<Member> afterDelete = memberService.findByUserName(member.getUsername());
        assertThat(afterDelete).isNotEmpty();
        assertThat(afterDelete.get().isDeleted()).isTrue();
    }


    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("회원 수정 폼")
    void showMemberModifyTest() throws Exception {

        ResultActions resultActions = mvc
                .perform(post("/member/editMyPage")
                        .with(csrf())
                        .param("nickname", "유저1닉네임")
                        .param("email", "user1@google.com")
                ).andDo(print());

        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("editMyPage"))
                .andExpect(status().is3xxRedirection());

        Optional<Member> findMember = memberService.findByUserName("user1");
        assertThat(findMember.get().getEmail()).isEqualTo("user1@google.com");
        assertThat(findMember.get().getNickname()).isEqualTo("유저1닉네임");
    }

    @Test
    @WithUserDetails(value = "user1")
    @DisplayName("회원 수정")
    void memberModifyTest() throws Exception {

        ResultActions resultActions = mvc
                .perform(post("/member/editMyPage")
                        .with(csrf())
                        .param("nickname", "유저1닉네임")
                        .param("email", "user1@google.com")
                ).andDo(print());

        resultActions
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("editMyPage"))
                .andExpect(status().is3xxRedirection());

        Optional<Member> findMember = memberService.findByUserName("user1");
        assertThat(findMember.get().getEmail()).isEqualTo("user1@google.com");
        assertThat(findMember.get().getNickname()).isEqualTo("유저1닉네임");
    }


    /*
    @Test
    @DisplayName("아이디찾기 폼")
    void t3() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/member/findUsername"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("showFindUsername"))
                .andExpect(content().string(containsString("아이디 찾기")));
    }

    @Test
    @DisplayName("아이디찾기")
    void t4() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/member/findUsername")
                        .with(csrf())
                        .param("email", "user1@test.com")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("findUsername"))
                .andExpect(redirectedUrlPattern("/member/login?**"));
    }

    @Test
    @DisplayName("비밀번호 찾기 폼")
    void t5() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/member/findPassword"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("showFindPassword"))
                .andExpect(content().string(containsString("비밀번호 찾기")));
    }

    @Test
    @DisplayName("비밀번호 찾기")
    void t6() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/member/findPassword")
                        .with(csrf())
                        .param("email", "user1@test.com")
                        .param("username", "user1")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("findPassword"))
                .andExpect(redirectedUrlPattern("/member/login?**"));
    }*/


}

