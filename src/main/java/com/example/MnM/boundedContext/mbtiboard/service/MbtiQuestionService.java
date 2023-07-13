package com.example.MnM.boundedContext.mbtiboard.service;

import com.example.MnM.boundedContext.mbtiboard.entity.mbtianswer.MbtiAnswer;
import com.example.MnM.boundedContext.mbtiboard.entity.mbtiquestion.MbtiDataNotFoundException;
import com.example.MnM.boundedContext.mbtiboard.entity.mbtiquestion.MbtiQuestion;
import com.example.MnM.boundedContext.mbtiboard.repository.MbtiQuestionRepository;
import com.example.MnM.boundedContext.member.entity.Member;
import com.nimbusds.oauth2.sdk.util.StringUtils;
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

    public Page<MbtiQuestion> getList(int page, String kw, String filterMbti, String sort) {
        List<Sort.Order> sorts = new ArrayList<>();

        if (StringUtils.isNotBlank(sort)) {
            switch (sort.toLowerCase()) {
                case "popular":
                    sorts.add(Sort.Order.desc("view"));
                    break;
                case "least_popular":
                    sorts.add(Sort.Order.asc("view"));
                    break;
                default:
                    sorts.add(Sort.Order.desc("createDate"));
                    break;
            }
        } else {
            sorts.add(Sort.Order.desc("createDate"));
        }

        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        Specification<MbtiQuestion> spec = search(kw, filterMbti);

        return mbtiQuestionRepository.findAll(spec, pageable);
    }

    private Specification<MbtiQuestion> search(String kw, String filterMbti) {
        return (Specification<MbtiQuestion>) (root, query, criteriaBuilder) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();

            if (kw != null && !kw.trim().isEmpty()) {
                Join<MbtiQuestion, Member> u1 = root.join("member", JoinType.LEFT);
                Join<MbtiQuestion, MbtiAnswer> a = root.join("answerList", JoinType.LEFT);
                Join<MbtiAnswer, Member> u2 = a.join("member", JoinType.LEFT);
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(root.get("subject"), "%" + kw + "%"),
                        criteriaBuilder.like(root.get("content"), "%" + kw + "%"),
                        criteriaBuilder.like(u1.get("nickname"), "%" + kw + "%"),
                        criteriaBuilder.like(a.get("content"), "%" + kw + "%"),
                        criteriaBuilder.like(u2.get("nickname"), "%" + kw + "%")
                ));
            }

            if (filterMbti != null && !filterMbti.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("mbti"), filterMbti));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
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
