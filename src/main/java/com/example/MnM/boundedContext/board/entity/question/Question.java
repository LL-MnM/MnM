package com.example.MnM.boundedContext.board.entity.question;

import com.example.MnM.base.baseEntity.BaseEntity;
import com.example.MnM.boundedContext.board.entity.answer.Answer;
import com.example.MnM.boundedContext.member.entity.Member;
import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;

@Getter
@Entity
public class Question extends BaseEntity {
    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Answer> answerList;

    @ManyToOne
    @JoinColumn(name = "member_nickname")
    private Member member;

    @ManyToMany
    private Set<Member> userId = new LinkedHashSet<>();

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
        this.userId.add(voter);
    }

    public void setView(int view) {
        this.view = view;
    }
}
