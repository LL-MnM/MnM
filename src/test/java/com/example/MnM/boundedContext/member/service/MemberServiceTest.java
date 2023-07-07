package com.example.MnM.boundedContext.member.service;

import com.example.MnM.base.rsData.RsData;
import com.example.MnM.boundedContext.member.dto.MemberDto;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.repository.MemberRepository;
import groovy.transform.builder.Builder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class MemberServiceTest {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;


    private String testUrl =  "https://nagtstorage.kr.object.ncloudstorage.com/USER/nagil";

    MultipartFile mockFile = new MockMultipartFile(
            "filename",   // 파일 이름
            "filename.jpg",   // 실제 이미지 파일의 확장자와 일치하도록 파일 이름을 설정합니다.
            MediaType.IMAGE_JPEG_VALUE,  // Content-Type 헤더에 이미지 타입을 지정합니다.
            "content".getBytes()  // 파일 내용을 바이트 배열로 전달
    );


    @BeforeEach
    void beforeEach() {
        Member memberTest = Member.builder()
                .username("test1")
                .password(passwordEncoder.encode("1234"))
                .email("test1@test.com")
                .profileImage(testUrl)
                .build();
        memberRepository.save(memberTest);
    }

    @Test
    @DisplayName("회원가입")
    @Builder
    public void joinTest() {
        String username = "test001";
        String password = "1234";
        String email = "test0010@test.com";
        MemberDto memberDto = MemberDto.builder().username(username).password(password).email(email).build();
        memberService.join(memberDto, "MnM");

        Optional<Member> member = memberService.findByUserName(username);
        assertThat(member.get()).isNotNull();
        assertThat(member.get().getUsername()).isEqualTo(username);
        assertThat(member.get().getEmail()).isEqualTo(email);

    }

    /*@Test
    @DisplayName("회원 수정")
    @Builder
    public void modifyTest() {
        Optional<Member> member = memberService.findByUserName("test1");
        MemberDto memberDto = MemberDto.builder().username("modifytest1").password(passwordEncoder.encode("1234")).email("test010@test.com").build();
        memberService.modify(member.get(), memberDto);

        Optional<Member> member1 = memberService.findByUserName("modifytest1");

        assertThat(member1.get().getUsername()).isEqualTo("modifytest1");


    }*/

    @Test
    @DisplayName("탈퇴")
    @Builder
    public void softDeletseTest() {
        memberService.deleteMember(memberService.findByUserName("test1").get());

        Optional<Member> afterDelete = memberService.findByUserName("test1");
        assertThat(afterDelete).isNotEmpty();
        assertThat(afterDelete.get().isDeleted()).isTrue();
    }

}