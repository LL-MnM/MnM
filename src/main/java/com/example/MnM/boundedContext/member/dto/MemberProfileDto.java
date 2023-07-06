package com.example.MnM.boundedContext.member.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter
@ToString
@Setter
@RequiredArgsConstructor
@Builder
public class MemberProfileDto {

    private MultipartFile profileImage;

}
