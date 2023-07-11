package com.example.MnM.boundedContext.email.entity;


import com.example.MnM.base.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
public class SendEmailLog extends BaseEntity {
    private String resultCode; //
    private String message; //
    private String email;
    private String subject;
    private String body;
    private LocalDateTime sendEndDate; //
    private LocalDateTime failDate; //

    public void setSuccessSendEmailLog(String resultCode, String message, LocalDateTime dateTime){
        this.resultCode = resultCode;
        this.message = message;
        this.sendEndDate  = dateTime;
    }

    public void setFailSendEmailLog(String resultCode, String message, LocalDateTime dateTime){
        this.resultCode = resultCode;
        this.message = message;
        this.failDate  = dateTime;
    }
}
