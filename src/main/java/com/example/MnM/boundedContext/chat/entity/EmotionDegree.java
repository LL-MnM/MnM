package com.example.MnM.boundedContext.chat.entity;

import com.example.MnM.base.baseEntity.BaseEntity;
import com.example.MnM.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
@Entity
public class EmotionDegree extends BaseEntity {

    private float magnitude;
    private float score;
    private String mbti;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void addMember(Member member) {
        this.member =member;
    }
}

