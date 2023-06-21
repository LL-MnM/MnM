package com.example.MnM.boundedContext.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@SuperBuilder
@NoArgsConstructor
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String username; //id
    private String password; //pw
    private String name; //이름
    private String nickname; //닉네임
    @Email
    private String email; //이메일
    private String providerType; //소셜로그인을 위한 제공자 타입
    @CreatedDate
    private LocalDateTime createdAt; //생성일
    @LastModifiedDate
    private LocalDateTime updatedAt; //수정일
    //여기까지 회원가입시 기본정보, 아래로는 개인정보
    private Long height; //키
    private Long age; //나이;
    private String locate; //지역
    private String gender; //성별
    private String mbti; //mbti
    private String hobby; //취미
    private String profileImage; //프로필사진, 임시로 만듬
    private String introduce; //자기소개

}