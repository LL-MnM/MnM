package com.example.MnM.boundedContext.board.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<Question> oq = questionRepository.findById(id);

        if (oq.isPresent() == false) throw new DataNotFoundException("question not found");

        return oq.get();
    }
    public Question create(String subject, String content) {
        Question q = new Question();
        q.createQuestion(subject, content);
        questionRepository.save(q);
        return q;
    }
}