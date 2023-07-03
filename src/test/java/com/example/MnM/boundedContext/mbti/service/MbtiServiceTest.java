package com.example.MnM.boundedContext.mbti.service;

import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.repository.MemberRepository;
import com.example.MnM.boundedContext.recommend.service.MemberMbtiService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class MbtiServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberMbtiService memberMbtiService;

    @Test
    @DisplayName("recommendMember")
    void recommendTest() {
        Optional<Member> name = memberRepository.findByName("홍길동");

        List<Member> mbtiForMember = memberMbtiService.findMbtiForMember(name.get());
        for (Member member : mbtiForMember) {
            String memberName = member.getName();
            assertThat(memberName).isEqualTo("김철수");
        }

    }
}