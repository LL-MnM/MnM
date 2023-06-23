package com.example.MnM.boundedContext.board.question;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestion is a Querydsl query type for Question
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestion extends EntityPathBase<Question> {

    private static final long serialVersionUID = -1443007363L;

    public static final QQuestion question = new QQuestion("question");

    public final ListPath<com.example.MnM.boundedContext.board.answer.Answer, com.example.MnM.boundedContext.board.answer.QAnswer> answerList = this.<com.example.MnM.boundedContext.board.answer.Answer, com.example.MnM.boundedContext.board.answer.QAnswer>createList("answerList", com.example.MnM.boundedContext.board.answer.Answer.class, com.example.MnM.boundedContext.board.answer.QAnswer.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath subject = createString("subject");

    public QQuestion(String variable) {
        super(Question.class, forVariable(variable));
    }

    public QQuestion(Path<? extends Question> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQuestion(PathMetadata metadata) {
        super(Question.class, metadata);
    }

}

