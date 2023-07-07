package com.example.MnM.boundedContext.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@AllArgsConstructor
@Getter
@ToString
@Setter
@RequiredArgsConstructor
@Builder
public class MemberDto {

    @NotBlank
    @Pattern(regexp = "^(?!.*admin)[A-Za-z0-9]+$", message = "영문자와 숫자로만 이루어져야 하며, 단어 'admin'을 사용할 수 없습니다.")
    private String username;
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
    private Integer age;

    private String locate; //지역

    private String gender; //성별

    private String mbti; //mbti

    private String hobby; //취미

    private String introduce; //자기소개

    private MultipartFile profileImage;

    private boolean deleted;
}
