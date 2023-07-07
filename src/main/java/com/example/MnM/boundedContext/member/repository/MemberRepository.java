package com.example.MnM.boundedContext.member.repository;

import com.example.MnM.boundedContext.member.entity.Member;

import jakarta.persistence.Entity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    //Optional<Member> findByUsername(String username); //아이디로 찾기

    Optional<Member> findByName(String name); //

    List<Member> findByMbti(String mbti);


    @Query("SELECT m FROM Member m where m.username =:username AND m.deleteDate IS NULL")
    Optional<Member> findByUsername(@Param("username") String username);

    @Modifying(flushAutomatically = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @Query(value = "DELETE FROM member WHERE id = ?1", nativeQuery = true)
    void deleteHardById(Long id);

    Optional<Member> findByUsernameAndEmail(String userId, String email);

    Optional<Member> findByEmail(String email);

}