package com.example.MnM.base.objectStorage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectRepository extends JpaRepository<ObjectFile, Long> {
    ObjectFile findByUrl(String url);
}
