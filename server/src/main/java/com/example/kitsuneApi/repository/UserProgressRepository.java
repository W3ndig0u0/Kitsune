package com.example.kitsuneApi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kitsuneApi.model.UserProgress;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    Optional<UserProgress> findByUserIdAndMediaConsumetId(Long userId, String consumetId);

}
