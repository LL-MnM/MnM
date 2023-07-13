package com.example.MnM.boundedContext.mbtiboard.entity.mbtianswer;

import com.example.MnM.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "MbtiAnswerVote")
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MbtiVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mbtiAnswer_id")
    private MbtiAnswer mbtiAnswer; // 변수명 수정

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_nickname")
    private Member member;


    public void setMbtiAnswer(MbtiAnswer mbtiAnswer) {
        this.mbtiAnswer = mbtiAnswer;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
