package com.example.MnM.boundedContext.board.question;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/question/list")
    public String list(Model model) {
        List<Question> questionList = questionService.getList();

        model.addAttribute("questionList", questionList);

        return "board/question_list";
    }
    @GetMapping("/question/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {
        Question question = questionService.getQuestion(id);

        model.addAttribute("question", question);

        return "board/question_detail";
    }
    @GetMapping("question/create")
    public String questionCreate() {
        return "board/question_form";
    }

    @PostMapping("question/create")
    public String questionCreate(@Valid QuestionForm questionForm) {

        questionService.create(questionForm.getSubject() , questionForm.getContent());

        return "redirect:/question/list";
    }
}

