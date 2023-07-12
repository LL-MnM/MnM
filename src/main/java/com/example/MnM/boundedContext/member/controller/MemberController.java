package com.example.MnM.boundedContext.member.controller;

import com.example.MnM.base.rq.Rq;
import com.example.MnM.base.rsData.RsData;
import com.example.MnM.boundedContext.member.dto.MemberDto;
import com.example.MnM.boundedContext.member.dto.MemberProfileDto;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.service.MemberService;
import com.example.MnM.boundedContext.recommend.service.MemberMbtiService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.context.ApplicationEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberMbtiService memberMbtiService;
    private final Rq rq;
    private final ApplicationEventPublisher publisher;


    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin() {
        return "member/login";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin() {
        return "member/join";
    }


    @PostMapping("/join")
    public String join(@Valid MemberDto memberDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return rq.redirectWithMsg("join", "회원가입 실패, 입력하신 정보를 다시 확인해주세요.");
        }
        RsData<Member> joinRs = memberService.join(memberDto);
        if (joinRs.isFail()) {
            return rq.redirectWithMsg("join",joinRs);
        }

        return rq.redirectWithMsg("login", joinRs.getMsg());
    }


    @GetMapping("/me")
    public String showMe(Model model) {
        Optional<Member> member = memberService.findByUserName(rq.getMember().getUsername());
        if(member.isEmpty()){return rq.redirectWithMsg("login", "회원이 없습니다");}
        model.addAttribute("member", member.get());
        return "member/me";
    }



    @GetMapping("/delete")
    public String MemberDelete() {
        Optional<Member> member = memberService.findByUserName(rq.getMember().getUsername());

        RsData<Member> rsData =  memberService.delete(member.get());
        rq.deleteCooKie();
        rq.sessionRefresh(rq.getReq(), rq.getResp());
        return rq.redirectWithMsg("/", rsData);
    }


    @GetMapping("/editMyPage")
    public String showEditMyPage(Model model) {
        Member member = rq.getMember();
        model.addAttribute("member", member);
        return "member/editMyPage";
    }


    @PostMapping("/editMyPage")
    public String editMyPage(@Valid MemberDto memberDto, BindingResult bindingResult) {
        Optional<Member> member = memberService.findByUserName(rq.getMember().getUsername());

        RsData<Member> rsData = memberService.modify(member.get(), memberDto);
        //Todo : 사용자 정보 갱신
        return rq.redirectWithMsg("me", "회원 정보를 수정하였습니다.");
    }


    @GetMapping("/editProfile")
    public String showEditProfile(Model model) {
        Member member = rq.getMember();
        model.addAttribute("member", member);
        return "member/editProfile";
    }


    @PostMapping("/editProfile")
    public String showEditProfile(@Valid MemberProfileDto memberProfileDto, BindingResult bindingResult) {
        Optional<Member> member = memberService.findByUserName(rq.getMember().getUsername());

        RsData<Member> memberRsData= memberService.modifyProfile(member.get(), memberProfileDto);
        //Todo : 사용자 정보 갱신
        return rq.redirectWithMsg("me", memberRsData);
    }


    @GetMapping("/emailVerification")
    public String showEmailVerification() {
        return "member/emailVerification";
    }


    @PostMapping("/emailVerification")
    public String emailVerification(String email) {
        Member member = rq.getMember();

        if (member.getEmail() != null && !member.getEmail().equals(email)) {
            return rq.historyBack(RsData.of("F-1", "기존 회원정보와 다른 이메일 주소입니다. 회원정보를 변경하거나, 기존 이메일 주소로 이메일 인증을 진행해주세요."));
        }

        RsData verificationRsData = memberService.sendVerificationMail(member, email);
        return rq.redirectWithMsg("me", verificationRsData);
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/findUsername")
    public String showFindUserId() {
        return "member/findUsername";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/findUsername")
    public String findUserId(String email) {
        Member member = memberService.findByEmail(email).orElse(null);

        if (member == null) {
            return rq.historyBack(RsData.of("F-1", "해당 이메일로 가입된 계정이 존재하지 않습니다."));
        }

        String foundedUsername = member.getUsername();
        String successMsg = "해당 이메일로 가입한 계정의 아이디는 '%s' 입니다.".formatted(foundedUsername);

        return rq.redirectWithMsg("login?username=%s".formatted(foundedUsername), RsData.of("S-1", successMsg));
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/findPassword")
    public String showFindPassword() {
        return "member/findPassword";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/findPassword")
    public String findPassword(String username, String email) {
        Optional<Member> member = memberService.findByUsernameAndEmail(username, email);

        if (member.isEmpty()) {
            RsData userNotFoundRsData = RsData.of("F-1", "일치하는 회원이 존재하지 않습니다.");
            return rq.historyBack(userNotFoundRsData);
        }

        RsData sendTempLoginPwToEmailResultData = memberService.sendTempPasswordToEmail(member.get());

        if (sendTempLoginPwToEmailResultData.isFail()) {
            return rq.historyBack(sendTempLoginPwToEmailResultData);
        }

        return rq.redirectWithMsg("login", sendTempLoginPwToEmailResultData);
    }


    @GetMapping("/modifyPassword")
    public String showModifyPassword() {
        return "member/modifyPassword";
    }


    @PostMapping("/modifyPassword")
    public String modifyPassword(String oldPassword, String password) {
        Member member = rq.getMember();
        RsData modifyRsData = memberService.modifyPassword(member, password, oldPassword);

        if (modifyRsData.isFail()) {
            return rq.historyBack(modifyRsData);
        }
        return rq.redirectWithMsg("me", modifyRsData);
    }

}
