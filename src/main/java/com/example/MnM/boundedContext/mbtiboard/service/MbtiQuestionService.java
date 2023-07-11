package com.example.MnM.boundedContext.mbtiboard.service;

import com.example.MnM.boundedContext.mbtiboard.entity.mbtianswer.MbtiAnswer;
import com.example.MnM.boundedContext.mbtiboard.entity.mbtiquestion.MbtiDataNotFoundException;
import com.example.MnM.boundedContext.mbtiboard.entity.mbtiquestion.MbtiQuestion;
import com.example.MnM.boundedContext.mbtiboard.repository.MbtiQuestionRepository;
import com.example.MnM.boundedContext.member.entity.Member;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class MbtiQuestionService {
    private final MbtiQuestionRepository mbtiQuestionRepository;

    public Page<MbtiQuestion> getList(int page, String kw) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        if ( kw == null || kw.trim().length() == 0 ) {
            return mbtiQuestionRepository.findAll(pageable);
        }

        Specification<MbtiQuestion> spec = search(kw);
        return mbtiQuestionRepository.findAll(spec, pageable);
    }

    private Specification<MbtiQuestion> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<MbtiQuestion> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);
                Join<MbtiQuestion, Member> u1 = q.join("member", JoinType.LEFT);
                Join<MbtiQuestion, MbtiAnswer> a = q.join("answerList", JoinType.LEFT);
                Join<MbtiAnswer, Member> u2 = a.join("member", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"),
                        cb.like(q.get("content"), "%" + kw + "%"),
                        cb.like(u1.get("nickname"), "%" + kw + "%"),
                        cb.like(a.get("content"), "%" + kw + "%"),
                        cb.like(u2.get("nickname"), "%" + kw + "%"));
            }
        };
    }

    public MbtiQuestion getMbtiQuestion(Integer id) {
        Optional<MbtiQuestion> question = this.mbtiQuestionRepository.findById(id);
        if (question.isPresent()) {
            MbtiQuestion question1 = question.get();
            question1.setView(question1.getView() + 1);
            this.mbtiQuestionRepository.save(question1);
            return question1;
        } else {
            throw new MbtiDataNotFoundException("question not found");
        }
    }

    public MbtiQuestion create(String subject, String content, Member member, String mbti) {
        MbtiQuestion q = new MbtiQuestion();
        q.createQuestion(subject, content, member, mbti);
        mbtiQuestionRepository.save(q);
        return q;
    }

    public void modify(MbtiQuestion question, String subject, String content, String mbti) {
        question.updateQuestion(subject, content, mbti);
        mbtiQuestionRepository.save(question);
    }

    public void delete(MbtiQuestion question) {
        mbtiQuestionRepository.delete(question);
    }

    public void vote(MbtiQuestion question, Member voter) {
        question.addVoter(voter);
    }
}

