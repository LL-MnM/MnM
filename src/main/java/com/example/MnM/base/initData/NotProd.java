package com.example.MnM.base.initData;

import com.example.MnM.boundedContext.member.controller.MemberController;
import com.example.MnM.boundedContext.member.dto.MemberDto;
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
            MemberService memberService
    ) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {

                Member member1 = memberService.join(new MemberDto(
                        "user1", "1234", "user1@email.com", "홍길동", "홍길동", 170, 20, "서울", "남자", "ENFP",
                        "운동", "안녕하세요", ""
                )).getData();


                Member member2 = memberService.join(new MemberDto(
                        "user3", "1234", "user2@email.com", "임꺽정", "임꺽정", 170, 20, "서울", "남자", "ENFP",

                        "운동", "안녕하세요", ""
                )).getData();

                //임의로 회원 생성
            }
        };
    }
}
