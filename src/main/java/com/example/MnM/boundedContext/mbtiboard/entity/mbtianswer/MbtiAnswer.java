package com.example.MnM.boundedContext.mbtiboard.entity.mbtianswer;

import com.example.MnM.base.baseEntity.BaseEntity;
import com.example.MnM.boundedContext.mbtiboard.entity.mbtiquestion.MbtiQuestion;
import com.example.MnM.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class MbtiAnswer extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private MbtiQuestion question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_nickname")
    private Member member;
    @OneToMany(mappedBy = "mbtiAnswer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MbtiVote> votes = new ArrayList<>();

    public MbtiAnswer(String content, LocalDateTime createDate, MbtiQuestion question, Member member) {
        this.content = content;
        this.createDate = createDate;
        this.question = question;
        this.member = member;

    }

    public void updateAnswer(String content) {
        this.content = content;
    }

    public void addVoter(Member voter) {
        MbtiVote newVote = new MbtiVote();
        newVote.setMbtiAnswer(this);
        newVote.setMember(voter);
        votes.add(newVote);
    }
}

