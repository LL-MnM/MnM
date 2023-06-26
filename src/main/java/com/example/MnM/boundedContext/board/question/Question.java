package com.example.MnM.boundedContext.board.question;


import com.example.MnM.boundedContext.board.answer.Answer;
import jakarta.persistence.*;
import java.util.List;
import lombok.ToString;
import com.example.MnM.boundedContext.board.BaseEntity;


import lombok.Getter;


import java.time.LocalDateTime;


@Getter
@Entity
@ToString
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    public void createQuestion(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }
}
