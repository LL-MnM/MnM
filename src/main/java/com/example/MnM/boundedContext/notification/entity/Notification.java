package com.example.MnM.boundedContext.notification.entity;

import com.example.MnM.base.baseEntity.BaseEntity;
import com.example.MnM.boundedContext.member.entity.Member;
import com.example.MnM.standard.util.Ut;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Notification extends BaseEntity {
    private LocalDateTime readDate;
    @ManyToOne
    @ToString.Exclude
    private Member toMember; // 알람 받는 사람
    @ManyToOne
    @ToString.Exclude
    private Member fromMember; // 알람을 발생시킨 행위를 한 사람

    private String url;

    public boolean isRead() {
        return readDate != null;
    }

    public void markAsRead() {
        readDate = LocalDateTime.now();
    }

    public String getCreateDateAfterStrHuman() {
        return Ut.time.diffFormat1Human(LocalDateTime.now(), getCreateDate());
    }

    public boolean isHot() {
        // 만들어진지 60분이 안되었다면 hot 으로 설정
        return getCreateDate().isAfter(LocalDateTime.now().minusMinutes(60));
    }
}
