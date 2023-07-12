package com.example.MnM.boundedContext.mbtiboard.repository;

import com.example.MnM.boundedContext.mbtiboard.entity.mbtianswer.MbtiAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MbtiAnswerRepository extends JpaRepository<MbtiAnswer, Integer> {
    @Transactional
    @Modifying
    @Query(value = "ALTER TABLE mbti_answer AUTO_INCREMENT = 1", nativeQuery = true)
    void clearAutoIncrement();
}
