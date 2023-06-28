package com.example.MnM.boundedContext.chat.entity;

import com.example.MnM.base.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class EmotionDegree extends BaseEntity {

    private String tendency;
    private float magnitude;
    private float score;

    public EmotionDegree(String tendency,float magnitude, float score) {
        this.tendency =tendency;
        this.magnitude = magnitude;
        this.score = score;
    }
}
