package com.example.MnM.likeablePerson.service;

import com.example.MnM.boundedContext.likeablePerson.entity.LikeablePerson;
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

import java.util.Optional;

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
    @DisplayName("호감 생성")
    void createLike() throws Exception {

        Optional<Member> memberUser3 = memberService.findByName("홍길동");

        likeablePersonService.like(memberUser3.get(), "임꺽정");
        assertThat(likeablePersonRepository.count() == 1);
    }

    @Test
    @DisplayName("호감 삭제")
        // 따로 실행하면 성공합니다.
    void deleteLike() throws Exception {

        Optional<Member> memberUser3 = memberService.findByName("홍길동");

        likeablePersonService.like(memberUser3.get(), "임꺽정");
        assertThat(likeablePersonRepository.count() == 1);
        Optional<LikeablePerson> byId = likeablePersonService.findById(1L);
        likeablePersonService.cancel(byId.get(), 1L);
        assertThat(likeablePersonRepository.count() == 0);
    }

    @Test
    @DisplayName("호감 수정")
        // 따로 실행하면 성공합니다.
    void modifyLike() throws Exception {

        Optional<Member> memberUser3 = memberService.findByName("홍길동");

        likeablePersonService.like(memberUser3.get(), "임꺽정");
        assertThat(likeablePersonRepository.findById(1L).get().getToMember().getName().equals("임꺽정"));
        likeablePersonService.modify(memberUser3.get(), 1L, "박준수");
        assertThat(likeablePersonRepository.findById(1L).get().getToMember().getName().equals("박준수"));

    }
}
