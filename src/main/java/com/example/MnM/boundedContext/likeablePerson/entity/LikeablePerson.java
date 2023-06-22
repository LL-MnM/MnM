package com.example.MnM.boundedContext.likeablePerson.entity;

import com.example.MnM.boundedContext.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class LikeablePerson {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;
    @ManyToOne
    @ToString.Exclude
    private Member fromMember; // 호감을 표시한 멤버

    @ManyToOne
    @ToString.Exclude
    private Member toMember; // 호감을 받은 멤버


}
