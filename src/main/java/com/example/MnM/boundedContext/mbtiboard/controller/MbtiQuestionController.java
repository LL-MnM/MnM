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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MbtiQuestionController {
    private final MbtiQuestionService mbtiQuestionService;
    private final Rq rq;
    private final MemberRepository memberRepository;

    @GetMapping("/mbti/question/list")
    public String list(Model model, @RequestParam(defaultValue = "0") int page, String kw) {
        Page<MbtiQuestion> paging = mbtiQuestionService.getList(page, kw);

        model.addAttribute("paging", paging);

        return "mbtiboard/mbtiquestion_list"; // 변경됨
    }
    @GetMapping("/mbti/question/detail/{id}") // 변경됨
    public String detail(Model model, @PathVariable("id") Integer id, MbtiAnswerForm mbtianswerForm) {
        MbtiQuestion mbtiQuestion = mbtiQuestionService.getMbtiQuestion(id);

        model.addAttribute("mbtiQuestion", mbtiQuestion);
        model.addAttribute("mbtiAnswerForm", mbtianswerForm);

        return "mbtiboard/mbtiquestion_detail"; // 변경됨
    }
    @GetMapping("/mbti/question/create") // 변경됨
    public String questionCreate(Model model) {
        model.addAttribute("mbtiQuestionForm", new MbtiQuestionForm());

        return "mbtiboard/mbtiquestion_form"; // 변경됨
    }

    @PostMapping("/mbti/question/create") // 변경됨
    public String questionCreate(@Valid MbtiQuestionForm mbtiQuestionForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return "mbtiboard/mbtiquestion_form"; // 변경됨
        }

        mbtiQuestionService.create(mbtiQuestionForm.getSubject() , mbtiQuestionForm.getContent(), rq.getMember());

        return "redirect:/mbti/question/list"; // 변경됨
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mbti/question/modify/{id}") // 변경됨
    public String questionModify(MbtiQuestionForm mbtiQuestionForm, @PathVariable("id") Integer id) {
        MbtiQuestion mbtiQuestion = this.mbtiQuestionService.getMbtiQuestion(id);
        if (!mbtiQuestion.getMember().getUsername().equals(rq.getMember().getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        mbtiQuestionForm.setSubject(mbtiQuestion.getSubject());
        mbtiQuestionForm.setContent(mbtiQuestion.getContent());
        return "mbtiboard/mbtiquestion_form"; // 변경됨
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/mbti/question/modify/{id}") // 변경됨
    public String questionModify(@Valid MbtiQuestionForm mbtiQuestionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "mbtiboard/mbtiquestion_form"; // 변경됨
        }
        MbtiQuestion mbtiQuestion = this.mbtiQuestionService.getMbtiQuestion(id);
        if (!mbtiQuestion.getMember().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.mbtiQuestionService.modify(mbtiQuestion, mbtiQuestionForm.getSubject(), mbtiQuestionForm.getContent());
        return String.format("redirect:/mbti/question/detail/%s", id); // 변경됨
    }
    @GetMapping("/mbti/question/delete/{id}") // 변경됨
    @PreAuthorize("isAuthenticated()")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        MbtiQuestion mbtiQuestion = this.mbtiQuestionService.getMbtiQuestion(id);

        if (!mbtiQuestion.getMember().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        mbtiQuestionService.delete(mbtiQuestion);

        return "redirect:/mbti/question/list"; // 변경됨
    }
    @GetMapping("/mbti/question/vote/{id}") // 변경됨
    @PreAuthorize("isAuthenticated()")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        MbtiQuestion mbtiQuestion = mbtiQuestionService.getMbtiQuestion(id);

        String username = principal.getName();
        Member voter = memberRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("회원 정보 없음"));

        mbtiQuestionService.vote(mbtiQuestion, voter);

        return String.format("redirect:/mbti/question/detail/%d", id); // 변경됨
    }
}
