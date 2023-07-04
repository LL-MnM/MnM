package com.example.MnM.boundedContext.chat.entity;

import com.example.MnM.base.baseEntity.BaseEntity;
import com.example.MnM.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class EmotionDegree extends BaseEntity {

    private String mbti;
    private float magnitude;
    private float score;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;


    public boolean isSmallerThan(float magnitude, float score) {
        return this.score < score;
    }

    public void updateTotal(float magnitude, float score) {
        this.magnitude =magnitude;
        this.score =score;
    }
}

