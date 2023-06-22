package com.example.MnM.base.initData;

import com.example.MnM.boundedContext.likeablePerson.service.LikeablePersonService;
import com.example.MnM.boundedContext.member.controller.MemberController;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Profile({"dev", "test"})
public class NotProd {
    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            LikeablePersonService likeablePersonService
    ) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                //임의로 회원 생성
                Member member1 = memberService.join(new MemberController.JoinForm(
                        "user1", "1234", "user1@email.com", "username1", "nickname1", 170, 20, "서울", "남자", "ENFP",
                        "운동", "안녕하세요", ""
                )).getData();

                Member member2 = memberService.join(new MemberController.JoinForm(
                        "user2", "1234", "user2@email.com", "username2", "nickname2", 170, 20, "서울", "남자", "ENFP",
                        "운동", "안녕하세요", ""
                )).getData();

                Member member3 = memberService.join(new MemberController.JoinForm(
                        "user3", "1234", "user3@email.com", "username3", "nickname3", 170, 20, "서울", "남자", "ENFP",
                        "운동", "안녕하세요", ""
                )).getData();

                Member member4 = memberService.join(new MemberController.JoinForm(
                        "user4", "1234", "user4@email.com", "username4", "nickname4", 170, 20, "서울", "남자", "ENFP",
                        "운동", "안녕하세요", ""
                )).getData();

                Member member5 = memberService.join(new MemberController.JoinForm(
                        "user5", "1234", "user5@email.com", "username5", "nickname5", 170, 20, "서울", "남자", "ENFP",
                        "운동", "안녕하세요", ""
                )).getData();

                //호감 생성
                likeablePersonService.like(member1, "username2");
                likeablePersonService.like(member1, "username3");

            }
        };
    }
}
