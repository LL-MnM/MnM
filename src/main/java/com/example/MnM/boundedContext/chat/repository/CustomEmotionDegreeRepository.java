package com.example.MnM.boundedContext.chat.repository;

import com.example.MnM.boundedContext.chat.entity.EmotionDegree;

public interface CustomEmotionDegreeRepository {
    EmotionDegree findBestMbti(Long memberId);
}
