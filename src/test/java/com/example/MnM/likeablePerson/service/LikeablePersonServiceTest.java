package com.example.MnM.likeablePerson.service;

import com.example.MnM.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.example.MnM.boundedContext.likeablePerson.service.LikeablePersonService;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class LikeablePersonServiceTest {
    @Autowired
    private LikeablePersonService likeablePersonService;
    @Autowired
    private MemberService memberService;

    @Autowired
    private LikeablePersonRepository likeablePersonRepository;

    @Test
    @DisplayName("좋아요 생성")
    void t001() throws Exception {

        Member memberUser3 = memberService.findByUserName("홍길동").orElseThrow();
        likeablePersonService.like(memberUser3, "임꺽정");
        assertThat(likeablePersonRepository.count() == 1);
    }

    @Test
    @DisplayName("좋아요 삭제")
    void t002() throws Exception {

        Member memberUser3 = memberService.findByUserName("홍길동").orElseThrow();
        likeablePersonService.like(memberUser3, "임꺽정");
        assertThat(likeablePersonRepository.count() == 1);
        likeablePersonService.findById(1L);
        assertThat(likeablePersonRepository.count() == 0);
    }
}
