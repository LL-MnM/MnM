package com.example.MnM.boundedContext.email.repository;


import com.example.MnM.boundedContext.email.entity.SendEmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendEmailLogRepository extends JpaRepository<SendEmailLog, Long> {
}
