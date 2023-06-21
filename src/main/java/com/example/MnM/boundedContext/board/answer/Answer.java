package com.example.MnM.boundedContext.board.answer;




import com.example.MnM.boundedContext.board.question.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.time.LocalDateTime;


@Getter
@Setter
@Entity
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
}
