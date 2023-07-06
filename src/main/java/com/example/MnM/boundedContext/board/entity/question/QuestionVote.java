package com.example.MnM.boundedContext.board.entity.question.vote;

import com.example.MnM.boundedContext.board.entity.question.Question;
import com.example.MnM.boundedContext.member.entity.Member;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class QuestionVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter_id")
    private Member voter;

    public QuestionVote(Question question, Member voter) {
        this.question = question;
        this.voter = voter;
    }
}
