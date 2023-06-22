package com.example.MnM.boundedContext.board.answer;


import ch.qos.logback.core.joran.util.beans.BeanDescriptionFactory;
import com.example.MnM.boundedContext.board.question.Question;
import com.example.MnM.boundedContext.board.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {
    private final QuestionService questionService;
    private final AnswerService answerService;

    @PostMapping("/create/{id}")
    public String createAnswer(Model model, @PathVariable Integer id, @RequestParam String content) {

        Question question = questionService.getQuestion(id);

        answerService.create(question, content);

        return "redirect:/question/detail/%d".formatted(id);
    }
}
