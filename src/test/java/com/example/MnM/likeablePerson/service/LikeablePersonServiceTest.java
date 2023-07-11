package com.example.MnM.likeablePerson.service;

import com.example.MnM.base.rsData.RsData;
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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class LikeablePersonServiceTest {
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
    void deleteLike() throws Exception {

        Optional<Member> memberUser3 = memberService.findByName("홍길동");

        likeablePersonService.like(memberUser3.get(), "임꺽정");

        Optional<LikeablePerson> byId = likeablePersonService.findById(1L);

        byId.ifPresent(person -> {
            likeablePersonService.cancel(person, 1L);
            assertThat(likeablePersonRepository.count() == 0);
        });
    }

    @Test
    @DisplayName("호감 수정")
    void modifyLike() throws Exception {

        Optional<Member> memberUser3 = memberService.findByName("홍길동");

        if (memberUser3.isPresent()) {
            RsData<LikeablePerson> rsData = likeablePersonService.like(memberUser3.get(), "임꺽정");
            assertThat(rsData.getData().getToMember().getName().equals("임꺽정"));

        Optional<LikeablePerson> byId = likeablePersonService.findById(1L);

        if (byId.isPresent()) {
            likeablePersonService.modify(memberUser3.get(), 1L, "김나리");
            assertThat(byId.get().getToMember().getName().equals("김나리"));
        }
        }
    }
}
