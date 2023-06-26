package com.example.MnM.boundedContext.board.answer;


import com.example.MnM.base.rq.Rq;
import com.example.MnM.boundedContext.board.question.Question;
import com.example.MnM.boundedContext.board.question.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final Rq rq;

    @PostMapping("/create/{id}")
    public String createAnswer(
            Model model,
            @PathVariable Integer id,
            @Valid AnswerForm answerForm,
            BindingResult bindingResult
    ) {

        Question question = questionService.getQuestion(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "board/question_detail";
        }
        Answer answer = answerService.create(question, answerForm.getContent());

        return rq.redirectWithMsg("/question/detail/%d".formatted(id), "확인용 메세지입니다.");
    }
}
