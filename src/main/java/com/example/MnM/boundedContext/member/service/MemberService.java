package com.example.MnM.boundedContext.member.service;

import com.example.MnM.base.rsData.RsData;
import com.example.MnM.boundedContext.member.dto.MemberDto;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public RsData<Member> join(MemberDto memberDto){ //일반 로그인
        return join(memberDto, "MnM");
    }

    @Transactional
    public RsData<Member> join(MemberDto memberDto, String providerTypeCode) {
        String username = memberDto.getUsername();
        String password = memberDto.getPassword();
        String name = memberDto.getName();
        String email = memberDto.getEmail();
        String nickname = memberDto.getNickname();
        Integer height = memberDto.getHeight();
        Integer age = memberDto.getAge();
        String locate =memberDto.getLocate();
        String gender = memberDto.getGender();
        String mbti = memberDto.getMbti();
        String hobby = memberDto.getHobby();
        String introduce = memberDto.getIntroduce();



        if (findByUserName(username).isPresent() || username.equals("admin")) {
            return RsData.of("F-1", "해당 아이디(%s)는 이미 사용중입니다.".formatted(username));
        }

        if (StringUtils.hasText(password)) password = passwordEncoder.encode(password);


        Member member = Member
                .builder()
                .username(username)
                .name(name)
                .password(password)
                .email(email)
                .nickname(nickname)
                .providerType(providerTypeCode)
                .age(age)
                .height(height)
                .gender(gender)
                .hobby(hobby)
                .mbti(mbti)
                .locate(locate)
                .introduce(introduce)
                .createDate(LocalDateTime.now())
                .build();

        return RsData.of("S-1", "회원가입이 완료되었습니다.", memberRepository.save(member));
    }


    /*public Optional<Member> findByName(String name) { //유저 이름으로 찾기
        return memberRepository.findByName(name);
    }*/

    public Optional<Member> findByUserName(String username) {//유저 아이디로 찾기
        return memberRepository.findByUsername(username);
    }

    public Member saveMember(Member member){
        return memberRepository.save(member);
    }

    public void deleteMember(Member member){
        memberRepository.delete(member);
    }

    public Optional<Member> findByMbti(String mbti){return memberRepository.findByMbti(mbti); }

    @Transactional
    public RsData<Member> whenSocialLogin(OAuth2User oAuth2User, String username, String providerTypeCode) {
        Optional<Member> opMember = findByUserName(username);

        if (opMember.isPresent()) return RsData.of("S-2", "로그인 되었습니다.", opMember.get());

        String name = null;
        String email = null;

        switch (providerTypeCode) {
            case "NAVER" -> {
                name = ((Map<String, String>) oAuth2User.getAttribute("response")).get("name");
                email = ((Map<String, String>) oAuth2User.getAttribute("response")).get("email");
            }
            case "KAKAO" -> {
                name = ((Map<String, String>) oAuth2User.getAttribute("properties")).get("nickname");
                email = ((Map<String, String>) oAuth2User.getAttribute("kakao_account")).get("email");

                if (!((Map<String, Map<String, Object>>) oAuth2User.getAttribute("kakao_account")).get("profile").get("is_default_image").equals("true")) {
                }
            }
            case "GOOGLE" -> {
                name = oAuth2User.getAttribute("name");
                email = oAuth2User.getAttribute("email");
            }
        }

        MemberDto memberDto = new MemberDto(username, "", providerTypeCode);

        return join(memberDto, providerTypeCode);
    }
}