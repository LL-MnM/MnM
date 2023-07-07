package com.example.MnM.base.security;

import com.example.MnM.base.appConfig.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    @Autowired
    private final CustomUserDetailsService userDetailsService;





    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/member/login")
                )
                .logout(
                        logout -> logout
                                .logoutUrl("/member/logout")
                )
                .oauth2Login(
                        oauth2Login -> oauth2Login
                                .loginPage("/member/login")
                )
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                       .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                                .hasAuthority("ADMIN")
                        .requestMatchers("/mbtiTest", "member/me", "member/editMyPage", "member/delete", "member/editProfile")
                                .hasAuthority("USER")
                                .anyRequest()
                                .permitAll()
                )//추후 관리자 페이지나 api문서 화면, 등등 설정함
                .rememberMe(rememberMe -> rememberMe //쿠키 적용, 아이디 기억하기 기능
                        .rememberMeParameter("remember")
                        .key(AppConfig.getKey()) //쿠키 키 값
                        .userDetailsService(userDetailsService)
                        .tokenValiditySeconds(AppConfig.getTokenValiditySeconds()) //쿠키는 3일짜리 입니다.
                );

        //TODO csrf 설정은 테스트용으로만 사용하고 나중에 다시 잠글겁니다.
        http.csrf( i -> i.ignoringRequestMatchers("/make","/show","/delete"));

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        //정적 자원들은 스프링 시큐리티 적용에서 제외
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

}