package com.example.MnM.boundedContext.board.service;

import java.util.ArrayList;
import java.util.List;

import com.example.MnM.boundedContext.board.entity.question.DataNotFoundException;
import com.example.MnM.boundedContext.board.entity.question.Question;
import com.example.MnM.boundedContext.board.repository.QuestionRepository;
import com.example.MnM.boundedContext.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public Page<Question> getList(int page) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        return questionRepository.findAll(pageable);
    }

    public Question getQuestion(Integer id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("question not found"));
    }
    public Question create(String subject, String content, Member member) {

        System.out.println(member.getUsername());
        System.out.println(member.getId());
        System.out.println(member.getNickname());

        Question q = new Question();
        q.createQuestion(subject, content , member);
        questionRepository.save(q);
        return q;
    }
}