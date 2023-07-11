package com.example.MnM.boundedContext.mbtiboard.entity.mbtiquestion;

import com.example.MnM.base.baseEntity.BaseEntity;
import com.example.MnM.boundedContext.mbtiboard.entity.mbtianswer.MbtiAnswer;
import com.example.MnM.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
@EqualsAndHashCode(callSuper = true, of = "id")
@Entity
public class MbtiQuestion extends BaseEntity {
    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    // 추가한 MBTI 유형 속성
    @Column
    private String mbti;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MbtiAnswer> answerList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_nickname")
    private Member member;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MbtiQuestionVote> voters;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view;

    // MBTI 유형을 파라미터로 추가
    public void createQuestion(String subject, String content, Member member, String mbti) {
        this.subject = subject;
        this.content = content;
        this.member = member;
        this.mbti = mbti; // MBTI 유형 설정
    }

    // MBTI 유형을 파라미터로 추가 (필요한 경우)
    public void updateQuestion(String subject, String content, String mbti) {
        this.subject = subject;
        this.content = content;
        this.mbti = mbti; // MBTI 유형 업데이트
    }

    public void addVoter(Member voter) {
        MbtiQuestionVote mbtiQuestionVote = new MbtiQuestionVote(this, voter);
        voters.add(mbtiQuestionVote);
    }

    public void setView(int view) {
        this.view = view;
    }
}
