package com.example.MnM.boundedContext.member.service;

import com.example.MnM.base.rsData.RsData;
import com.example.MnM.boundedContext.member.controller.MemberController;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.repository.MemberRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RsData<Member> join(MemberController.JoinForm joinForm) {
        String userId = joinForm.getUserId();
        String username = joinForm.getUsername();
        String password = joinForm.getPassword();
        String email = joinForm.getEmail();
        String nickname = joinForm.getNickname();
        Long height = joinForm.getHeight();
        Long age = joinForm.getAge();
        String locate =joinForm.getLocate();
        String gender = joinForm.getGender();
        String mbti = joinForm.getMbti();
        String hobby = joinForm.getHobby();
        String introduce = joinForm.getIntroduce();



        if (findByUserId(userId).isPresent()) {
            return RsData.of("F-1", "해당 아이디(%s)는 이미 사용중입니다.".formatted(userId));
        }

        if (StringUtils.hasText(password)) password = passwordEncoder.encode(password);

        Member member = Member
                .builder()
                .username(username)
                .userId(userId)
                .password(password)
                .email(email)
                .nickname(nickname)
                .providerType(null)
                .age(age)
                .height(height)
                .gender(gender)
                .hobby(hobby)
                .mbti(mbti)
                .locate(locate)
                .introduce(introduce)
                .build();

        return RsData.of("S-1", "회원가입이 완료되었습니다.", memberRepository.save(member));
    }


    public Optional<Member> findByUserId(String username) { //유저 아이디로 찾기
        return memberRepository.findByUserId(username);
    }
}