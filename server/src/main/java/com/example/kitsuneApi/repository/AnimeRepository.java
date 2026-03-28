package com.example.kitsuneApi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kitsuneApi.model.UserProgress;

@Repository
public interface AnimeRepository extends JpaRepository<UserProgress, Long> {
    List<UserProgress> findByUserId(String userId);

    List<UserProgress> findByUserId(Long userId);
}