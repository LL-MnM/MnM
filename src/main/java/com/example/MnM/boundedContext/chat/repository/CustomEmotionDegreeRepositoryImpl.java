package com.example.MnM.boundedContext.chat.repository;

import com.example.MnM.boundedContext.chat.entity.EmotionDegree;
import com.example.MnM.boundedContext.chat.entity.QEmotionDegree;
import com.example.MnM.boundedContext.member.entity.QMember;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static com.example.MnM.boundedContext.chat.entity.QEmotionDegree.*;
import static com.example.MnM.boundedContext.member.entity.QMember.*;

@RequiredArgsConstructor
@Repository
public class CustomEmotionDegreeRepositoryImpl implements CustomEmotionDegreeRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public EmotionDegree findBestMbti(Long memberId) {

        BooleanExpression equalMember = isEqualMember(memberId);

        return jpaQueryFactory.selectFrom(emotionDegree)
                .join(emotionDegree.member, member)
                .where(equalMember)
                .orderBy(emotionDegree.score.desc())
                .limit(1)
                .fetchOne();
    }

    private BooleanExpression isEqualMember(Long memberId) {
        return memberId != null ?  member.id.eq(memberId) : null;
    }

}
