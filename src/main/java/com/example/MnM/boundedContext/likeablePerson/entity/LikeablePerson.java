package com.example.MnM.boundedContext.likeablePerson.entity;

import com.example.MnM.base.baseEntity.BaseEntity;
import com.example.MnM.boundedContext.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class LikeablePerson extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Member fromMember; // 호감을 표시한 멤버

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Member toMember; // 호감을 받은 멤버
}
