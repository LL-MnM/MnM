package com.example.MnM.boundedContext.board.controller;

import com.example.MnM.base.rq.Rq;
import com.example.MnM.boundedContext.board.entity.answer.AnswerForm;
import com.example.MnM.boundedContext.board.entity.question.Question;
import com.example.MnM.boundedContext.board.entity.question.QuestionForm;
import com.example.MnM.boundedContext.board.service.QuestionService;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.boundedContext.member.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final Rq rq;
    private final MemberRepository memberRepository;

    @GetMapping("/question/list")
    public String list(Model model , @RequestParam(defaultValue = "0") int page,String kw) {
        Page<Question> paging = questionService.getList(page, kw);

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


        questionService.create(questionForm.getSubject() , questionForm.getContent(),rq.getMember());

        return "redirect:/question/list";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("question/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getMember().getUsername().equals(rq.getMember().getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "board/question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("question/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "board/question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getMember().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("question/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);

        if (!question.getMember().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        questionService.delete(question);

        return "redirect:/";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("question/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Question question = questionService.getQuestion(id);

        String username = principal.getName();
        Member voter = memberRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("회원 정보 없음"));

        questionService.vote(question, voter);

        return "redirect:/question/detail/%d".formatted(id);
    }
}
