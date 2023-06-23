package com.example.MnM.boundedContext.board.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuestionForm {

    @NotBlank
    @Size(max = 100)
    private String subject;

    @NotBlank
    @Size(max = 200)
    private String content;

}
