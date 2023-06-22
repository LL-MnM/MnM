package com.example.MnM.boundedContext.likeablePerson.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLikeablePerson is a Querydsl query type for LikeablePerson
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLikeablePerson extends EntityPathBase<LikeablePerson> {

    private static final long serialVersionUID = 495666898L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLikeablePerson likeablePerson = new QLikeablePerson("likeablePerson");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final com.example.MnM.boundedContext.member.entity.QMember fromMember;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> modifyDate = createDateTime("modifyDate", java.time.LocalDateTime.class);

    public final com.example.MnM.boundedContext.member.entity.QMember toMember;

    public QLikeablePerson(String variable) {
        this(LikeablePerson.class, forVariable(variable), INITS);
    }

    public QLikeablePerson(Path<? extends LikeablePerson> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLikeablePerson(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLikeablePerson(PathMetadata metadata, PathInits inits) {
        this(LikeablePerson.class, metadata, inits);
    }

    public QLikeablePerson(Class<? extends LikeablePerson> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.fromMember = inits.isInitialized("fromMember") ? new com.example.MnM.boundedContext.member.entity.QMember(forProperty("fromMember")) : null;
        this.toMember = inits.isInitialized("toMember") ? new com.example.MnM.boundedContext.member.entity.QMember(forProperty("toMember")) : null;
    }

}

