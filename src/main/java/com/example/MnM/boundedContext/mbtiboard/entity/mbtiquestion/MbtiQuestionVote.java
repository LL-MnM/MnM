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
    private MbtiQuestion question; // 'Question'을 'MbtiQuestion'으로 수정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter_id")
    private Member voter;

    public MbtiQuestionVote(MbtiQuestion question, Member voter) { // 생성자 파라미터 타입 수정
        this.question = question;
        this.voter = voter;
    }
}
