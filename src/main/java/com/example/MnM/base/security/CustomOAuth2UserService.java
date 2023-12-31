package com.example.MnM.base.security;

import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberService memberService;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String providerTypeCode = userRequest.getClientRegistration().getRegistrationId().toUpperCase();

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String oauthId = switch (providerTypeCode) {
            case "NAVER" -> ((Map<String, String>) oAuth2User.getAttribute("response")).get("id");
            case "KAKAO" -> oAuth2User.getAttribute("id").toString();
            case "GOOGLE" -> oAuth2User.getAttribute("sub");
            default -> oAuth2User.getName();
        };

        String userId = providerTypeCode + "__%s".formatted(oauthId);

        Member member = memberService.whenSocialLogin(oAuth2User, userId, providerTypeCode).getData();

        return new CustomOAuth2User(member.getUsername(), member.getPassword(), member.getGrantedAuthorities());
    }
}

class CustomOAuth2User extends User implements OAuth2User {

    public CustomOAuth2User(String userId, String password, Collection<? extends GrantedAuthority> authorities) {
        super(userId, password, authorities);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public String getName() {
        return getUsername();
    }
}