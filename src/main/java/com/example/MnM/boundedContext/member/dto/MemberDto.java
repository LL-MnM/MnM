package com.example.MnM.boundedContext.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.*;


@AllArgsConstructor
@Getter
@ToString
@Setter
@RequiredArgsConstructor
@Builder
public class MemberDto {

    @NotBlank(message = "id는 필수 입니다")
    private String username; //id
    @NotBlank
    private String password;
    @NotBlank
    private String email;
    @NotBlank
    private String name; //이름

    private String providerTypeCode;

    private String nickname; //닉네임
    @Positive
    private Integer height; //키
    @Positive
    private Integer age; //나이;

    private String locate; //지역

    private String gender; //성별

    private String mbti; //mbti

    private String hobby; //취미

    private String introduce; //자기소개

    private String profileImage;

    private boolean deleted;
}
