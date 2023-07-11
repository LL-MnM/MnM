package com.example.MnM.boundedContext.mbtiboard.entity.mbtiquestion;

import com.example.MnM.base.baseEntity.BaseEntity;
import com.example.MnM.boundedContext.mbtiboard.entity.mbtianswer.MbtiAnswer;
import com.example.MnM.boundedContext.mbtiboard.entity.mbtiquestion.MbtiQuestionVote;
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

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MbtiAnswer> answerList;  // Answer를 MbtiAnswer로 수정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_nickname")
    private Member member;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MbtiQuestionVote> voters;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view;

    public void createQuestion(String subject, String content, Member member) {
        this.subject = subject;
        this.content = content;
        this.member = member;
    }

    public void updateQuestion(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }

    public void addVoter(Member voter) {
        MbtiQuestionVote mbtiQuestionVote = new MbtiQuestionVote(this, voter);
        voters.add(mbtiQuestionVote);
    }

    public void setView(int view) {
        this.view = view;
    }
}
