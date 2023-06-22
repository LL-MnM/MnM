package com.example.MnM.boundedContext.likeablePerson.repository;

import com.example.MnM.boundedContext.likeablePerson.entity.LikeablePerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeablePersonRepository extends JpaRepository<LikeablePerson, Long> {

}
