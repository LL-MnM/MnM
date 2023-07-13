package com.example.MnM.boundedContext.mbtiboard.entity.mbtiquestion;

import com.example.MnM.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class MbtiQuestionVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private MbtiQuestion question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter_id")
    private Member voter;

    public MbtiQuestionVote(MbtiQuestion question, Member voter) {
        if (question.alreadyVoted(voter)) { // 추가됨
            throw new IllegalStateException("이미 투표한 회원입니다.");
        }

        this.question = question;
        this.voter = voter;
    }
}
