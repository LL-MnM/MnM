package com.example.MnM.boundedContext.board.entity.answer;

import com.example.MnM.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity(name="AnswerVote")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Answer answer;

    @ManyToOne
    @JoinColumn(name = "member_nickname")
    private Member member;

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}