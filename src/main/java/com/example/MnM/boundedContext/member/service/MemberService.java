package com.example.MnM.boundedContext.member.service;

import com.example.MnM.base.appConfig.AppConfig;
import com.example.MnM.base.objectStorage.service.AmazonService;
import com.example.MnM.base.objectStorage.service.S3FolderName;
import com.example.MnM.base.rsData.RsData;
import com.example.MnM.boundedContext.email.service.EmailService;
import com.example.MnM.boundedContext.emailVerification.service.EmailVerificationService;
import com.example.MnM.boundedContext.member.dto.MemberDto;
import com.example.MnM.boundedContext.member.dto.MemberProfileDto;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.repository.MemberRepository;
import com.example.MnM.boundedContext.recommend.service.MbtiService;
import com.example.MnM.standard.util.Ut;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final AmazonService amazonService;

    private final EmailVerificationService emailVerificationService;

    private final EmailService emailService;

    private final MbtiService mbtiService;


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
        MultipartFile profileImage  = memberDto.getProfileImage();
        String url = AppConfig.getDefaultURL();

        if(memberDto.getProfileImage() != null && Objects.requireNonNull(memberDto.getProfileImage().getContentType()).startsWith("image/")){
            url = fileUpLoad(profileImage, username);
        }


        if (findByUserName(username).isPresent()) {
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
                .profileImage(url)
                .build();

        Member saveMember = memberRepository.save(member);

        emailVerificationService.send(saveMember, email)
                .thenAccept(sendRsData -> {
                    // 성공시 처리
                })
                .exceptionally(error -> {
                    log.error(error.getMessage());
                    return null; // 에러 처리
                });

        return RsData.of("S-1", "회원가입이 완료되었습니다.", saveMember);
    }
    public String fileUpLoad(MultipartFile multipartFile, String username){
        return amazonService.uploadImage(multipartFile, S3FolderName.USER, username);
    }
    public void fileDelete(String username){
        amazonService.deleteImage(S3FolderName.USER, username);
    }

    public Optional<Member> findByName(String name) { //유저 이름으로 찾기
        return memberRepository.findByName(name);
    }

    public Optional<Member> findByUserName(String username) {//유저 아이디로 찾기
        return memberRepository.findByUsername(username);
    }



    // soft-delete
    public RsData<Member> delete(Member member) {
        Member deletedMember = member.toBuilder()
                .deleteDate(LocalDateTime.now())
                .build();
        fileDelete(member.getUsername());
        memberRepository.save(deletedMember);

        return RsData.of("S-1", "회원탈퇴 성공");
    }

    //hard delete
    @Transactional
    public RsData<Member> deleteMember(Member member) {
        memberRepository.delete(member);
        fileDelete(member.getUsername());
        return RsData.of("S-1", "회원탈퇴 성공");
    }

    //아직 사용하지 않았습니다 회원 수정시 시큐리티에 정보 반영하는 코드입니다
    private void forceAuthentication(Member member) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<GrantedAuthority> updatedAuthorities = (List<GrantedAuthority>) member.getGrantedAuthorities();

        Authentication newAuth = new UsernamePasswordAuthenticationToken(new User(member.getUsername(), member.getPassword(), member.getGrantedAuthorities()), auth.getCredentials(), updatedAuthorities);

        new User(member.getUsername(), member.getPassword(), member.getGrantedAuthorities());

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
    //아직 사용하지 않았습니다 회원 수정시 시큐리티에 정보 반영하는 코드입니다
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


    public RsData<Member> modify(Member member, MemberDto memberDto) {

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
                .build();

        memberRepository.save(modifiedMember);

        return RsData.of("S-1", "회원정보를 수정하였습니다");
    }

    public RsData<Member> modifyProfile(Member member, MemberProfileDto memberProfileDto) {
        if(memberProfileDto.getProfileImage().isEmpty()){
            return RsData.of("F-1", "프로필 사진이 없습니다.");
        }

        String url = fileUpLoad(memberProfileDto.getProfileImage(), member.getUsername());

        Member modifiedProfileMember = member.toBuilder()
                .profileImage(url)
                .build();

        return RsData.of("S-1", "프로필 사진 교체 완료", memberRepository.save(modifiedProfileMember));
    }

    @Transactional
    public RsData verifyEmail(long id, String verificationCode) {
        RsData verifyVerificationCodeRs = emailVerificationService.verifyVerificationCode(id, verificationCode);

        if (!verifyVerificationCodeRs.isSuccess()) {
            return verifyVerificationCodeRs;
        }

        Member member = memberRepository.findById(id).get();

        member.setMemberEmailVerified(true);

        return RsData.of("S-1", "이메일인증이 완료되었습니다.");
    }

    public RsData sendVerificationMail(Member actor, String email) {
        emailVerificationService.send(actor, email);
        return RsData.of("S-1", "인증 메일을 성공적으로 `%s` 주소로 전송했습니다.".formatted(email));
    }


    public Optional<Member> findByUsernameAndEmail(String userId, String email) {
        return memberRepository.findByUsernameAndEmail(userId, email);
    }

    @Transactional
    public RsData sendTempPasswordToEmail(Member actor) {
        String title = "[MnM] 임시 패스워드 발송";
        String tempPassword = Ut.getTempPassword(6);

        String body = """
                <!DOCTYPE html>
                <html>
                    <h1>임시 패스워드 : %s</h1>
                    <a href="http://localhost:8080/usr/member/login" target="_blank">로그인 하러가기</a>
                </html>
                """.formatted(tempPassword);

        RsData sendResultData = sendMail(actor.getEmail(), title, body);

        if (sendResultData.isFail()) {
            return sendResultData;
        }

        setTempPassword(actor, tempPassword);

        return RsData.of("S-1", "계정의 이메일주소로 임시 패스워드가 발송되었습니다.");
    }

    public RsData sendMail(String to, String title, String body) {
        return emailService.sendEmail(to, title, body);
    }

    //회원 수정, 삭제
    @Transactional
    public RsData<Member> setTempPassword(Member member, String tempPassword) {
        member.changePassword(passwordEncoder.encode(tempPassword));

        return RsData.of("S-1", "회원정보를 수정하였습니다");
    }

    @Transactional
    public RsData modifyPassword(Member member, String password, String oldPassword) {
        if (!passwordEncoder.matches(oldPassword, member.getPassword())) {
            return RsData.of("F-1", "기존 비밀번호가 일치하지 않습니다.");
        }
        member.changePassword(passwordEncoder.encode(password));

        return RsData.of("S-1", "비밀번호가 변경되었습니다.");
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

}