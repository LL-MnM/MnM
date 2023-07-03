package com.example.MnM.boundedContext.recommend.repository;

import com.example.MnM.boundedContext.recommend.entity.Mbti;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MbtiRepository extends JpaRepository<Mbti, Long> {
    Optional<Mbti> findByName(String mbti);
}
