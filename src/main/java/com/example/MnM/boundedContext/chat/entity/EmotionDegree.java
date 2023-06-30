package com.example.MnM.boundedContext.chat.entity;

import com.example.MnM.base.baseEntity.BaseEntity;
import com.example.MnM.boundedContext.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class EmotionDegree extends BaseEntity {

    private String tendency;
    private float magnitude;
    private float score;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public EmotionDegree(String tendency,float magnitude, float score) {
        this.tendency =tendency;
        this.magnitude = magnitude;
        this.score = score;
    }
}
