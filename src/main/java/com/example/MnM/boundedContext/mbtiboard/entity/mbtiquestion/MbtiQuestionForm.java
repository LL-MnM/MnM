package com.example.MnM.boundedContext.mbtiboard.entity.mbtiquestion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MbtiQuestionForm {

    @NotBlank(message = "제목은 필수항목입니다.")
    @Size(max = 100, message = "제목을 100자 이하로 입력해주세요.")
    private String subject;

    @NotBlank(message = "내용은 필수항목입니다.")
    @Size(max = 200, message = "내용을 200자 이하로 입력해주세요.")
    private String content;

    // 추가된 속성
    @NotBlank(message = "MBTI 유형은 필수항목입니다.")
    @Size(max = 4, min = 4, message = "MBTI 유형을 4자로 입력해주세요.")
    private String mbti;

}
