package com.example.MnM.boundedContext.mbtiboard.controller;

import com.example.MnM.base.rq.Rq;
import com.example.MnM.boundedContext.mbtiboard.entity.mbtianswer.MbtiAnswerForm;
import com.example.MnM.boundedContext.mbtiboard.entity.mbtiquestion.MbtiQuestion;
import com.example.MnM.boundedContext.mbtiboard.entity.mbtiquestion.MbtiQuestionForm;
import com.example.MnM.boundedContext.mbtiboard.service.MbtiQuestionService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MbtiQuestionController {
    private final MbtiQuestionService mbtiQuestionService;
    private final Rq rq;
    private final MemberRepository memberRepository;

    @GetMapping("/mbti/question/list")
    public String list(Model model,
                       @RequestParam(defaultValue = "0") int page,
                       String kw,
                       @RequestParam(defaultValue = "") String filterMbti,
                       @RequestParam(defaultValue = "latest") String sort) {
        Page<MbtiQuestion> paging = mbtiQuestionService.getList(page, kw, filterMbti, sort);

        model.addAttribute("paging", paging);

        return "mbtiboard/mbtiquestion_list";
    }

    @GetMapping("/mbti/question/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, MbtiAnswerForm mbtianswerForm) {
        MbtiQuestion mbtiQuestion = mbtiQuestionService.getMbtiQuestion(id);

        model.addAttribute("mbtiQuestion", mbtiQuestion);
        model.addAttribute("mbtiAnswerForm", mbtianswerForm);

        return "mbtiboard/mbtiquestion_detail";
    }

    @GetMapping("/mbti/question/create")
    public String questionCreate(Model model) {
        model.addAttribute("mbtiQuestionForm", new MbtiQuestionForm());

        return "mbtiboard/mbtiquestion_form";
    }

    @PostMapping("/mbti/question/create")
    public String questionCreate(@Valid MbtiQuestionForm mbtiQuestionForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "mbtiboard/mbtiquestion_form";
        }

        mbtiQuestionService.create(mbtiQuestionForm.getSubject(), mbtiQuestionForm.getContent(), rq.getMember(), mbtiQuestionForm.getMbti());

        return "redirect:/mbti/question/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mbti/question/modify/{id}")
    public String questionModify(MbtiQuestionForm mbtiQuestionForm, @PathVariable("id") Integer id) {
        MbtiQuestion mbtiQuestion = this.mbtiQuestionService.getMbtiQuestion(id);
        if (!mbtiQuestion.getMember().getUsername().equals(rq.getMember().getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        mbtiQuestionForm.setSubject(mbtiQuestion.getSubject());
        mbtiQuestionForm.setContent(mbtiQuestion.getContent());
        mbtiQuestionForm.setMbti(mbtiQuestion.getMbti());
        return "mbtiboard/mbtiquestion_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/mbti/question/modify/{id}")
    public String questionModify(@Valid MbtiQuestionForm mbtiQuestionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "mbtiboard/mbtiquestion_form";
        }
        MbtiQuestion mbtiQuestion = this.mbtiQuestionService.getMbtiQuestion(id);
        if (!mbtiQuestion.getMember().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.mbtiQuestionService.modify(mbtiQuestion, mbtiQuestionForm.getSubject(), mbtiQuestionForm.getContent(), mbtiQuestionForm.getMbti());
        return String.format("redirect:/mbti/question/detail/%s", id);
    }

    @GetMapping("/mbti/question/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        MbtiQuestion mbtiQuestion = this.mbtiQuestionService.getMbtiQuestion(id);

        if (!mbtiQuestion.getMember().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        mbtiQuestionService.delete(mbtiQuestion);

        return "redirect:/mbti/question/list";
    }

    @GetMapping("/mbti/question/vote/{id}")
    @PreAuthorize("isAuthenticated()")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        MbtiQuestion mbtiQuestion = mbtiQuestionService.getMbtiQuestion(id);

        String username = principal.getName();
        Member voter = memberRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("회원 정보 없음"));

        try {
            mbtiQuestionService.vote(mbtiQuestion, voter);
        } catch (IllegalStateException e) {

        }

        return String.format("redirect:/mbti/question/detail/%d", id);
    }
}
