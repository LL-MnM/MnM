package com.example.MnM.boundedContext.board.entity.answer;

import com.example.MnM.boundedContext.board.entity.question.Question;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @ManyToOne
    @ToString.Exclude
    private Question question;

    public Answer(String content, LocalDateTime createDate, Question question) {
        this.content = content;
        this.createDate = createDate;
        this.question = question;
    }
}

