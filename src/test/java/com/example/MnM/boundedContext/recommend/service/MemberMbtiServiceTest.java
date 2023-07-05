package com.example.MnM.boundedContext.recommend.service;

import com.example.MnM.boundedContext.chat.entity.EmotionDegree;
import com.example.MnM.boundedContext.chat.repository.EmotionDegreeRepository;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.repository.MemberRepository;
import com.example.MnM.boundedContext.recommend.entity.Mbti;
import com.example.MnM.boundedContext.recommend.repository.MbtiRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class MemberMbtiServiceTest {

    @Autowired
    MemberMbtiService memberMbtiService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EmotionDegreeRepository emotionDegreeRepository;

    @MockBean
    MbtiRepository mbtiRepository;

    @MockBean
    MbtiService mbtiService;


    @DisplayName("emotionDegree가 존재하지 않는 회원이 최고의 mbti 찾기")
    @Test
    void findBestMbtiNotExistsDegree() {

        String mbti = "mbti";
        int memberCount = 5;
        for (int i = 0; i < memberCount; i++) {
            memberRepository.save(Member.builder().mbti(mbti).build());
        }
        String testMbti = "testMbti";
        when(mbtiService.findByName(testMbti)).thenReturn(Mbti.builder().bestMbti(mbti).build());
        Member member = memberRepository.save(Member.builder().mbti(testMbti).build());
        List<Member> members = memberMbtiService.findMbtiForMember(member);

        assertThat(members).size().isEqualTo(memberCount);

    }

    @DisplayName("emotionDegree가 존재하는 회원이 최고의 mbti 찾기")
    @Test
    void findBestMbtiExistsDegree() {

        String mbti = "mbti";

        for (int i = 0; i < 5; i++) {
            memberRepository.save(Member.builder().mbti(mbti).build());
        }

        Member member = memberRepository.save(Member.builder().build());
        emotionDegreeRepository.save(EmotionDegree.builder()
                .member(member)
                .mbti("mbti")
                .build());
        List<Member> members = memberMbtiService.findMbtiForMember(member);
        assertThat(members).size().isEqualTo(5);
    }

}