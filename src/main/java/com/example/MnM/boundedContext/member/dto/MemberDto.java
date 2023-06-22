package com.example.MnM.boundedContext.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.*;


@AllArgsConstructor
@Getter
@ToString
@Setter
public class MemberDto {

    @NotEmpty
    private String userId; //id
    @NotEmpty
    private String password;
    @NotEmpty
    private String email;
    @NotEmpty
    private String username; //이름
    @NotEmpty
    private String nickname; //닉네임
    @Positive
    private Integer height; //키
    @Positive
    private Integer age; //나이;
    @NotEmpty
    private String locate; //지역
    @NotEmpty
    private String gender; //성별
    @NotEmpty
    private String mbti; //mbti
    @NotEmpty
    private String hobby; //취미
    @NotEmpty
    private String introduce; //자기소개

    private String profileImage;

}
