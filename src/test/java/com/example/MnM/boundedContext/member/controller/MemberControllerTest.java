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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
    public void Test() {
        member = Member.builder()
                .username("test001")
                .name("홍길동")
                .password("1234")
                .build();//testcase
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
    @DisplayName("회원 수정")
    void memberModify() throws Exception {

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
    @DisplayName("회원 탈퇴")
    void t003() throws Exception {
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
    @Builder
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
    @DisplayName("프로필 업로드")
    @Builder
    public void profileUploadTest() {


    }

    @Test
    @DisplayName("soft delete test")
    @Builder
    public void softDeletseTest() {

        memberRepository.save(member);
        assertThat(memberService.findByUserName(member.getUsername())).isNotNull();
        assertThat(member.isDeleted()).isFalse();

        memberService.deleteMember(member);

        Optional<Member> afterDelete = memberService.findByUserName(member.getUsername());
        assertThat(afterDelete).isNotEmpty();
        assertThat(afterDelete.get().isDeleted()).isTrue();
    }
}

