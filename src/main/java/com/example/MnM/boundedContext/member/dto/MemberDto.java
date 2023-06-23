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
public class MemberDto {

    @NotBlank
    private String userId; //id
    @NotBlank
    private String password;
    @NotBlank
    private String email;
    @NotBlank
    private String username; //이름
    @NotBlank
    private String nickname; //닉네임
    @Positive
    private Integer height; //키
    @Positive
    private Integer age; //나이;
    @NotBlank
    private String locate; //지역
    @NotBlank
    private String gender; //성별
    @NotBlank
    private String mbti; //mbti
    @NotBlank
    private String hobby; //취미
    @NotBlank
    private String introduce; //자기소개

    private String profileImage;

    private boolean deleted;

}
