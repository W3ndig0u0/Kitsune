package com.example.kitsuneApi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kitsuneApi.model.UserProgress;

@Repository
public interface AnimeRepository extends JpaRepository<UserProgress, Long> {
    List<UserProgress> findByUserId(String userId);

    Optional<UserProgress> findByUserIdAndAnimeId(String userId, String animeId);
}