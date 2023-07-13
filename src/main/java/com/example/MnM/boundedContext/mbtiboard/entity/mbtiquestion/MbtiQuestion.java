package com.example.MnM.boundedContext.mbtiboard.entity.mbtiquestion;

import com.example.MnM.base.baseEntity.BaseEntity;
import com.example.MnM.boundedContext.mbtiboard.entity.mbtianswer.MbtiAnswer;
import com.example.MnM.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashSet;
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

    @Column
    private String mbti;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MbtiAnswer> answerList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_nickname")
    private Member member;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MbtiQuestionVote> voters = new HashSet<>();


    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view;

    // MBTI 유형을 파라미터로 추가
    public void createQuestion(String subject, String content, Member member, String mbti) {
        this.subject = subject;
        this.content = content;
        this.member = member;
        this.mbti = mbti;
    }

    public void updateQuestion(String subject, String content, String mbti) {
        this.subject = subject;
        this.content = content;
        this.mbti = mbti;
    }

    public void addVoter(Member voter) {
        if (alreadyVoted(voter)) {
            throw new IllegalStateException("이미 투표한 회원입니다.");
        }

        MbtiQuestionVote mbtiQuestionVote = new MbtiQuestionVote(this, voter);
        voters.add(mbtiQuestionVote);
    }

    public boolean alreadyVoted(Member voter) {
        return voters.stream().anyMatch(vote -> vote.getVoter().equals(voter));
    }

    public void setView(int view) {
        this.view = view;
    }
}
