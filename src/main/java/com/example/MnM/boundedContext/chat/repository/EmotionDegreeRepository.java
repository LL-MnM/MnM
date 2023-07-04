package com.example.MnM.boundedContext.chat.repository;

import com.example.MnM.boundedContext.chat.entity.EmotionDegree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmotionDegreeRepository extends JpaRepository<EmotionDegree,Long>,CustomEmotionDegreeRepository {
    Optional<EmotionDegree> findByMemberIdAndMbti(Long memberId, String mbti);

    boolean existsByMemberId(Long memberId);

}
