package com.example.MnM.boundedContext.mbtiboard.repository;

import com.example.MnM.boundedContext.mbtiboard.entity.mbtiquestion.MbtiQuestion; // 임포트 구문 수정
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MbtiQuestionRepository extends JpaRepository<MbtiQuestion, Integer> {
    MbtiQuestion findBySubject(String subject);
    MbtiQuestion findBySubjectAndContent(String subject, String content);
    List<MbtiQuestion> findBySubjectLike(String subject);
    Page<MbtiQuestion> findAll(Specification<MbtiQuestion> spec, Pageable pageable);
}
