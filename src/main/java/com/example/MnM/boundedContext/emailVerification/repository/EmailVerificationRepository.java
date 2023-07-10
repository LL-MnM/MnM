package com.example.MnM.boundedContext.emailVerification.repository;


import com.example.MnM.boundedContext.emailVerification.Entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    Optional<EmailVerification> findByMemberId(long memberId);
}
