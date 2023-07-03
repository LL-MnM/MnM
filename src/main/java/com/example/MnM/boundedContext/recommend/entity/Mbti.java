package com.example.MnM.boundedContext.recommend.entity;

import com.example.MnM.base.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Mbti extends BaseEntity {
    private String name;
    private String bestMbti;
}
