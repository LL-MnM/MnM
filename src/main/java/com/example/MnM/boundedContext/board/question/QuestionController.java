package com.example.MnM.boundedContext.board.question;
import java.util.List;

import com.example.MnM.boundedContext.board.answer.AnswerForm;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/question/list")
    public String list(Model model , @RequestParam(defaultValue = "0") int page) {
        Page<Question> paging = questionService.getList(page);

        model.addAttribute("paging", paging);

        return "board/question_list";
    }
    @GetMapping("/question/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        Question question = questionService.getQuestion(id);

        model.addAttribute("question", question);
        model.addAttribute("answerForm",answerForm);

        return "board/question_detail";
    }
    @GetMapping("question/create")
    public String questionCreate(Model model) {
        model.addAttribute("questionForm", new QuestionForm());

        return "board/question_form";
    }

    @PostMapping("question/create")
    public String questionCreate(@Valid QuestionForm questionForm , BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return "board/question_form";
        }


        questionService.create(questionForm.getSubject() , questionForm.getContent());

        return "redirect:/question/list";
    }
}

