package com.example.MnM.boundedContext.member.controller;

import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class MemberControllerTest {
    @Autowired
    private MemberService memberService;


    @Test
    @DisplayName("soft delete test")
    public void softDeleteTest() {
        Member member = new Member("test001", "홍길동", "1234");//testcase

        memberService.saveMember(member);
        assertThat(memberService.findByUserId(member.getUserId())).isNotNull();
        assertThat(member.isDeleted()).isFalse();

        memberService.deleteMember(member);

        Optional<Member> afterDelete = memberService.findByUserId(member.getUserId());
        assertThat(afterDelete).isNotEmpty();
        assertThat(afterDelete.get().isDeleted()).isTrue();

    }

}