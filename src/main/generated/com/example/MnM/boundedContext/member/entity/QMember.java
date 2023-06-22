package com.example.MnM.boundedContext.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1681278394L;

    public static final QMember member = new QMember("member1");

    public final com.example.MnM.base.baseEntity.QBaseEntity _super = new com.example.MnM.base.baseEntity.QBaseEntity(this);

    public final NumberPath<Integer> age = createNumber("age", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createDate = _super.createDate;

    public final StringPath email = createString("email");

    public final ListPath<com.example.MnM.boundedContext.likeablePerson.entity.LikeablePerson, com.example.MnM.boundedContext.likeablePerson.entity.QLikeablePerson> fromLikeablePeople = this.<com.example.MnM.boundedContext.likeablePerson.entity.LikeablePerson, com.example.MnM.boundedContext.likeablePerson.entity.QLikeablePerson>createList("fromLikeablePeople", com.example.MnM.boundedContext.likeablePerson.entity.LikeablePerson.class, com.example.MnM.boundedContext.likeablePerson.entity.QLikeablePerson.class, PathInits.DIRECT2);

    public final StringPath gender = createString("gender");

    public final NumberPath<Integer> height = createNumber("height", Integer.class);

    public final StringPath hobby = createString("hobby");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath introduce = createString("introduce");

    public final StringPath locate = createString("locate");

    public final StringPath mbti = createString("mbti");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDate = _super.modifyDate;

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath profileImage = createString("profileImage");

    public final StringPath providerType = createString("providerType");

    public final ListPath<com.example.MnM.boundedContext.likeablePerson.entity.LikeablePerson, com.example.MnM.boundedContext.likeablePerson.entity.QLikeablePerson> toLikeablePeople = this.<com.example.MnM.boundedContext.likeablePerson.entity.LikeablePerson, com.example.MnM.boundedContext.likeablePerson.entity.QLikeablePerson>createList("toLikeablePeople", com.example.MnM.boundedContext.likeablePerson.entity.LikeablePerson.class, com.example.MnM.boundedContext.likeablePerson.entity.QLikeablePerson.class, PathInits.DIRECT2);

    public final StringPath userId = createString("userId");

    public final StringPath username = createString("username");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

