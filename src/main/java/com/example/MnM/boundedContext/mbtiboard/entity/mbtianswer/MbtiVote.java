package com.example.MnM.boundedContext.mbtiboard.entity.mbtianswer;

import com.example.MnM.boundedContext.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name="MbtiAnswerVote")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MbtiVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "mbtiAnswer_id")
    private MbtiAnswer mbtiAnswer; // 변수명 수정

    @ManyToOne
    @JoinColumn(name = "member_nickname")
    private Member member;

    // votes 필드와 관련된 코드를 삭제했습니다. 각 MbtiVote는 한 개의 MbtiAnswer와 연결됩니다.

    public void setMbtiAnswer(MbtiAnswer mbtiAnswer) {
        this.mbtiAnswer = mbtiAnswer;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
