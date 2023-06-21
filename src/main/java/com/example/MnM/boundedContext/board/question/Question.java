package com.example.MnM.boundedContext.board.question;


import com.example.MnM.boundedContext.board.answer.Answer;
import jakarta.persistence.*;
import java.util.List;
import lombok.ToString;

import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@ToString
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(length = 200)
    private String subject;


    @Column(columnDefinition = "TEXT")
    private String content;


    private LocalDateTime createDate;


    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;
}
