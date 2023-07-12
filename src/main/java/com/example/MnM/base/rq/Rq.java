package com.example.MnM.base.rq;

import com.example.MnM.base.rsData.RsData;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.service.MemberService;
import com.example.MnM.standard.util.Ut;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

@Component
@RequestScope
public class Rq {
    private final MemberService memberService;
    private final HttpServletRequest req;
    private final HttpServletResponse resp;
    private final HttpSession session;
    private final User user;
    private Member member = null; // 레이지 로딩, 처음부터 넣지 않고, 요청이 들어올 때 넣는다.

    public Rq(MemberService memberService, HttpServletRequest req, HttpServletResponse resp, HttpSession session) {
        this.memberService = memberService;
        this.req = req;
        this.resp = resp;
        this.session = session;

        // 현재 로그인한 회원의 인증정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof User) {
            this.user = (User) authentication.getPrincipal();
        } else {
            this.user = null;
        }
    }

    // 로그인 되어 있는지 체크
    public boolean isLogin() {
        return user != null;
    }

    // 로그아웃 되어 있는지 체크
    public boolean isLogout() {
        return !isLogin();
    }

    // 로그인 된 회원의 객체
    public Member getMember() {
        if (isLogout()) return null;
        // 데이터가 없는지 체크
        if (member == null) {
            member = memberService.findByUserName(user.getUsername()).orElse(null);
        }
        return member;
    }

    // 뒤로가기 + 메세지
    public String historyBack(String msg) {
        String referer = req.getHeader("referer");
        String key = "historyBackErrorMsg___" + referer;
        req.setAttribute("localStorageKeyAboutHistoryBackErrorMsg", key);
        req.setAttribute("historyBackErrorMsg", msg);
        // 200 이 아니라 400 으로 응답코드가 지정되도록
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return "common/js";
    }

    // 뒤로가기 + 메세지
    public String historyBack(RsData rsData) {
        return historyBack(rsData.getMsg());
    }

    // 302 + 메세지
    public String redirectWithMsg(String url, RsData rsData) {
        return redirectWithMsg(url, rsData.getMsg());
    }

    // 302 + 메세지
    public String redirectWithMsg(String url, String msg) {
        return "redirect:" + urlWithMsg(url, msg);
    }

    private String urlWithMsg(String url, String msg) {
        // 기존 URL에 혹시 msg 파라미터가 있다면 그것을 지우고 새로 넣는다.
        return Ut.url.modifyQueryParam(url, "msg", msgWithTtl(msg));
    }

    // 메세지에 ttl 적용
    private String msgWithTtl(String msg) {
        return Ut.url.encode(msg) + ";ttl=" + new Date().getTime();
    }

    public String getParamsJsonStr() {
        Map<String, String[]> parameterMap = req.getParameterMap();

        return Ut.json.toStr(parameterMap);
    }

    public HttpServletRequest getReq(){
        return this.req;
    }

    public HttpServletResponse getResp(){
        return this.resp;
    }

    public void sessionRefresh(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, auth);
        }
    }
}