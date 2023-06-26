package com.example.MnM.boundedContext.board;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@MappedSuperclass
public abstract class BaseEntity {
    @Column
    private LocalDateTime createDate;

    @Column
    private LocalDateTime updateDate;

    @PrePersist
    public void onCreate() {
        createDate = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updateDate = LocalDateTime.now();
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }
}
