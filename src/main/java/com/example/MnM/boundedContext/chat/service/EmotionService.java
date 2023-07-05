package com.example.MnM.boundedContext.chat.service;

import com.example.MnM.boundedContext.chat.dto.SentimentDto;
import com.example.MnM.boundedContext.chat.entity.EmotionDegree;
import com.example.MnM.boundedContext.chat.repository.EmotionDegreeRepository;
import com.example.MnM.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class EmotionService {

    private final EmotionDegreeRepository emotionDegreeRepository;

    public void saveIfLargerScore(Member member, String mbti, SentimentDto sentimentDto) {

        Optional<EmotionDegree> optional = emotionDegreeRepository.findByMemberIdAndMbti(member.getId(), mbti);

        float magnitude = sentimentDto.magnitude();
        float score = sentimentDto.score();

        if (optional.isEmpty())  {
            EmotionDegree firstDegree = EmotionDegree.builder()
                    .magnitude(magnitude)
                    .score(score)
                    .mbti(mbti)
                    .member(member)
                    .build();
            emotionDegreeRepository.save(firstDegree);
            return;
        }

        EmotionDegree findDegree = optional.get();
        if (findDegree.isSmallerThan(magnitude,score)) {
            findDegree.updateTotal(magnitude,score);
        }

    }

    public boolean isExistsEmotionDegree(Long id) {
        return emotionDegreeRepository.existsByMemberId(id);
    }

    public String findBestMbtiByDegree(Long memberId) {
        EmotionDegree bestMbti = emotionDegreeRepository.findBestMbti(memberId);
        return bestMbti.getMbti();
    }

}
