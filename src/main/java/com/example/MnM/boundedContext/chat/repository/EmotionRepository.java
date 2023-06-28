package com.example.MnM.boundedContext.chat.repository;

import com.example.MnM.boundedContext.chat.entity.EmotionDegree;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmotionRepository extends JpaRepository<EmotionDegree, Long> {
}
