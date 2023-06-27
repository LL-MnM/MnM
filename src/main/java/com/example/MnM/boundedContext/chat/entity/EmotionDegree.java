package com.example.MnM.boundedContext.chat.entity;

import com.example.MnM.base.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class EmotionDegree extends BaseEntity {

    private float magnitude;
    private float score;

    public EmotionDegree(float magnitude, float score) {
        this.magnitude = magnitude;
        this.score = score;
    }
}
