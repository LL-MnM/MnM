package com.example.MnM.boundedContext.member.service;

import com.example.MnM.base.rsData.RsData;
import com.example.MnM.boundedContext.member.dto.MemberDto;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.repository.MemberRepository;
import com.example.MnM.boundedContext.recommend.service.MbtiService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
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

    private final MbtiService mbtiService;


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
        String profileImage  = memberDto.getProfileImage();



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
                .providerType(providerTypeCode)
                .nickname(nickname)
                .age(age)
                .height(height)
                .gender(gender)
                .hobby(hobby)
                .mbti(mbti)
                .locate(locate)
                .introduce(introduce)
                .createDate(LocalDateTime.now())
                .profileImage(profileImage)
                .build();

        return RsData.of("S-1", "회원가입이 완료되었습니다.", memberRepository.save(member));
    }


    public Optional<Member> findByName(String name) { //유저 이름으로 찾기
        return memberRepository.findByName(name);
    }

    public Optional<Member> findByUserName(String username) {//유저 아이디로 찾기
        return memberRepository.findByUsername(username);
    }

    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }


    // soft-delete
    public void delete(Member member) {
        Member deletedMember = member.toBuilder()
                .deleteDate(LocalDateTime.now())
                .build();
        memberRepository.save(deletedMember);
    }

    //hard delete
    public RsData<Member> deleteMember(Member member){
        memberRepository.delete(member);
        return RsData.of("S-1", "회원탈퇴 성공");
    }

    private void forceAuthentication(Member member) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<GrantedAuthority> updatedAuthorities = (List<GrantedAuthority>) member.getGrantedAuthorities();

        Authentication newAuth = new UsernamePasswordAuthenticationToken(new User(member.getUsername(), member.getPassword(), member.getGrantedAuthorities()), auth.getCredentials(), updatedAuthorities);

        new User(member.getUsername(), member.getPassword(), member.getGrantedAuthorities());

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    public void renewAuthentication(Member member) {
        User user = new User(member.getUsername(), member.getPassword(), member.getGrantedAuthorities());

        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        user,
                        null,
                        member.getGrantedAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public List<Member> findByMbti(String mbti){return memberRepository.findByMbti(mbti); }

    @Transactional
    public RsData<Member> whenSocialLogin(OAuth2User oAuth2User, String username, String providerTypeCode) {
        Optional<Member> opMember = findByUserName(username);

        if (opMember.isPresent()) return RsData.of("S-2", "로그인 되었습니다.", opMember.get());

        MemberDto memberDto = MemberDto.builder()
                .username(username)
                .password("")
                .providerTypeCode(providerTypeCode)
                .build();
        return join(memberDto, providerTypeCode);
    }

    public Member modify(Member member, MemberDto memberDto) {
        Member modifiedMember = member.toBuilder()
                .name(memberDto.getName())
                .email(memberDto.getEmail())
                .nickname(memberDto.getNickname())
                .age(memberDto.getAge())
                .height(memberDto.getHeight())
                .gender(memberDto.getGender())
                .hobby(memberDto.getHobby())
                .mbti(memberDto.getMbti())
                .locate(memberDto.getLocate())
                .introduce(memberDto.getIntroduce())
                .createDate(LocalDateTime.now())
                .profileImage(memberDto.getProfileImage())
                .build();

        return memberRepository.save(modifiedMember);

    }
}