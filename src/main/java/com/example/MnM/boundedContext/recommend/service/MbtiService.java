package com.example.MnM.boundedContext.recommend.service;


import com.example.MnM.boundedContext.recommend.entity.Mbti;
import com.example.MnM.boundedContext.recommend.repository.MbtiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MbtiService {
    private final MbtiRepository mbtiRepository;

    public Mbti findByName(String mbti) {
        return mbtiRepository.findByName(mbti).orElseThrow();
    }
}
