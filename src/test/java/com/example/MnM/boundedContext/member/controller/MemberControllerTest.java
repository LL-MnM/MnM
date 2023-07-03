package com.example.MnM.boundedContext.member.controller;

import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.repository.MemberRepository;
import com.example.MnM.boundedContext.member.service.MemberService;
import groovy.transform.builder.Builder;
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
    private MemberRepository memberRepository;


    @Test
    @DisplayName("soft delete test")
    @Builder
    public void softDeleteTest() {
        Member member = Member.builder()
                .username("test001")
                .name("홍길동")
                .password("1234")
                .build();//testcase

        memberRepository.save(member);
        assertThat(memberService.findByUserName(member.getUsername())).isNotNull();
        assertThat(member.isDeleted()).isFalse();

        memberService.deleteMember(member);

        Optional<Member> afterDelete = memberService.findByUserName(member.getUsername());
        assertThat(afterDelete).isNotEmpty();
        assertThat(afterDelete.get().isDeleted()).isTrue();

    }

}