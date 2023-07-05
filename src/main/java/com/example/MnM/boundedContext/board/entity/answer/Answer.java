package com.example.MnM.boundedContext.board.entity.answer;

import com.example.MnM.base.baseEntity.BaseEntity;
import com.example.MnM.boundedContext.board.entity.question.Question;
import com.example.MnM.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Answer extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Question question;

    @ManyToOne
    @JoinColumn(name = "member_nickname")
    private Member member;

    public Answer(String content, LocalDateTime createDate, Question question,Member member) {
        this.content = content;
        this.createDate = createDate;
        this.question = question;
        this.member = member;

    }
    public void updateAnswer(String content) {
        this.content = content;
    }

    @ManyToMany
    private Set<Member> username = new LinkedHashSet<>();

    public void addVoter(Member voter) {
        username.add(voter);
    }
}
