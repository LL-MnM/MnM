package com.example.MnM.boundedContext.board.controller;


import com.example.MnM.base.rq.Rq;
import com.example.MnM.boundedContext.board.entity.answer.Answer;
import com.example.MnM.boundedContext.board.entity.answer.AnswerForm;
import com.example.MnM.boundedContext.board.service.AnswerService;
import com.example.MnM.boundedContext.board.entity.question.Question;
import com.example.MnM.boundedContext.board.service.QuestionService;
import com.example.MnM.boundedContext.member.entity.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

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
        Answer answer = answerService.create(question, answerForm.getContent(),rq.getMember());

        return rq.redirectWithMsg("/question/detail/%d".formatted(id), "확인용 메세지입니다.");
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
        Answer answer = answerService.getAnswer(id);

        if (!answer.getMember().getUserId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        answerForm.setContent(answer.getContent());

        return "board/answer_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult,
                               @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "board/answer_form";
        }

        Answer answer = answerService.getAnswer(id);

        if (!answer.getMember().getUserId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        answerService.modify(answer, answerForm.getContent());

        return "redirect:/question/detail/%d".formatted(answer.getQuestion().getId());
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
        Answer answer = answerService.getAnswer(id);

        if (!answer.getMember().getUserId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        answerService.delete(answer);

        return "redirect:/question/detail/%d".formatted(answer.getQuestion().getId());
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(Principal principal, @PathVariable("id") Integer id) {
        Answer answer = answerService.getAnswer(id);
        Member voter = answer.getMember();

        answerService.vote(answer, voter);

        return "redirect:/question/detail/%d".formatted(answer.getQuestion().getId());
    }
}
