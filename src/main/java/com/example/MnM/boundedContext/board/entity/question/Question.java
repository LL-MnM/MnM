package com.example.MnM.boundedContext.board.entity.question;

import com.example.MnM.base.baseEntity.BaseEntity;
import com.example.MnM.boundedContext.board.entity.answer.Answer;
import com.example.MnM.boundedContext.member.entity.Member;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true,of = "id")
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
    @JoinTable(name = "question_voter", joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "voter_id"))
    private Set<Member> voters = new HashSet<>();

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
        voters.add(voter);
    }

    public void setView(int view) {
        this.view = view;
    }
}
