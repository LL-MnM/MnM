package com.example.MnM.boundedContext.chat.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Embeddable
public class EmotionDegree {

    private float magnitude;
    private float score;
    private String mbti;
}

