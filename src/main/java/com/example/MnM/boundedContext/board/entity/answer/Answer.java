package com.example.MnM.boundedContext.board.entity.answer;

import com.example.MnM.base.baseEntity.BaseEntity;
import com.example.MnM.boundedContext.board.entity.question.Question;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

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

    public Answer(String content, LocalDateTime createDate, Question question) {
        this.content = content;
        this.createDate = createDate;
        this.question = question;
    }
}

