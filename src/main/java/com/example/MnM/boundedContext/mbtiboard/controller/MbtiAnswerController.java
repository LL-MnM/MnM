package com.example.MnM.boundedContext.mbtiboard.controller;

import com.example.MnM.base.rq.Rq;
import com.example.MnM.boundedContext.mbtiboard.entity.mbtianswer.MbtiAnswer;
import com.example.MnM.boundedContext.mbtiboard.entity.mbtianswer.MbtiAnswerForm;
import com.example.MnM.boundedContext.mbtiboard.entity.mbtiquestion.MbtiQuestion;
import com.example.MnM.boundedContext.mbtiboard.service.MbtiAnswerService;
import com.example.MnM.boundedContext.mbtiboard.service.MbtiQuestionService;
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
@RequestMapping("/mbti/answer")
@RequiredArgsConstructor
public class MbtiAnswerController {
    private final MbtiQuestionService mbtiQuestionService;
    private final MbtiAnswerService mbtiAnswerService;
    private final Rq rq;

    @PostMapping("/create/{id}")
    public String createAnswer(
            Model model,
            @PathVariable Integer id,
            @Valid MbtiAnswerForm mbtiAnswerForm,
            BindingResult bindingResult
    ) {

        MbtiQuestion mbtiQuestion = mbtiQuestionService.getMbtiQuestion(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("mbtiQuestionForm", mbtiQuestion);
            return "mbtiboard/mbtiquestion_detail";
        }
        MbtiAnswer answer = mbtiAnswerService.create(mbtiQuestion, mbtiAnswerForm.getContent(),rq.getMember());

        return "redirect:/mbti/question/detail/%d#answer_%d".formatted(id, answer.getId());
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String answerModify(MbtiAnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
        MbtiAnswer mbtiAnswer = mbtiAnswerService.getAnswer(id);

        mbtiAnswerService.checkAuthority(mbtiAnswer, principal.getName());

        answerForm.setContent(mbtiAnswer.getContent());

        return "mbtiboard/mbtianswer_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid MbtiAnswerForm answerForm, BindingResult bindingResult,
                               @PathVariable("id") Integer id, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "mbtiboard/mbtianswer_form";
        }

        MbtiAnswer mbtiAnswer = mbtiAnswerService.getAnswer(id);

        mbtiAnswerService.checkAuthority(mbtiAnswer, principal.getName());

        mbtiAnswerService.modify(mbtiAnswer, answerForm.getContent());

        return "redirect:/mbti/question/detail/%d#answer_%d".formatted(mbtiAnswer.getQuestion().getId(), id);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
        MbtiAnswer mbtiAnswer = mbtiAnswerService.getAnswer(id);

        mbtiAnswerService.checkAuthority(mbtiAnswer, principal.getName());

        mbtiAnswerService.delete(mbtiAnswer);

        return "redirect:/mbti/question/detail/%d".formatted(mbtiAnswer.getQuestion().getId());
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(Principal principal, @PathVariable("id") Integer id) {
        MbtiAnswer mbtiAnswer = mbtiAnswerService.getAnswer(id);
        Member voter = rq.getMember();

        mbtiAnswerService.vote(mbtiAnswer, voter);

        return "redirect:/mbti/question/detail/%d#answer_%d".formatted(mbtiAnswer.getQuestion().getId(), id);
    }

}
