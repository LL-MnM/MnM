package com.example.MnM.boundedContext.board.question;
import java.util.List;

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
    public String questionCreate(QuestionForm questionForm) {

        String subject = questionForm.getSubject();
        String content = questionForm.getContent();

        if (subject == null || subject.trim().length() == 0){
            throw new RuntimeException("subject(을)를 입력해주세요");
        }

        if ( subject.trim().length() > 100){
            throw new RuntimeException("subject(을)를 100자 이하로 입력해주세요");
        }

        if (content == null || content.trim().length() == 0){
            throw new RuntimeException("content(을)를 입력해주세요");
        }

        if ( content.trim().length() > 200){
            throw new RuntimeException("content(을)를 200자 이하로 입력해주세요");
        }

        questionService.create(subject, content);

        return "redirect:/question/list";
    }
}

