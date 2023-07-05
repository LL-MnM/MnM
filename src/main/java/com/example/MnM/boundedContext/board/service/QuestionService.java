package com.example.MnM.boundedContext.board.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.MnM.boundedContext.board.entity.question.DataNotFoundException;
import com.example.MnM.boundedContext.board.entity.question.Question;
import com.example.MnM.boundedContext.board.repository.QuestionRepository;
import jakarta.persistence.criteria.*;
import com.example.MnM.boundedContext.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.example.MnM.boundedContext.board.entity.answer.Answer;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public Page<Question> getList(int page, String kw) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        if ( kw == null || kw.trim().length() == 0 ) {
            return questionRepository.findAll(pageable);
        }

        Specification<Question> spec = search(kw);
        return questionRepository.findAll(spec, pageable);
    }

    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Question, Member> u1 = q.join("userId", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, Member> u2 = a.join("userId", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            Question question1 = question.get();
            question1.setView(question1.getView()+1);
            this.questionRepository.save(question1);
            return question1;
        } else {
            throw new DataNotFoundException("question not found");
        }
    }


    public Question create(String subject, String content, Member member) {

        System.out.println(member.getUsername());
        System.out.println(member.getId());
        System.out.println(member.getNickname());

        Question q = new Question();
        q.createQuestion(subject, content, member);
        questionRepository.save(q);
        return q;
    }
    public void modify(Question question, String subject, String content) {
        question.updateQuestion(subject,content);
        questionRepository.save(question);
    }
    public void delete(Question question) {
        questionRepository.delete(question);
    }
    public void vote(Question question, Member voter) {
        question.addVoter(voter);
        questionRepository.save(question);
    }
}
