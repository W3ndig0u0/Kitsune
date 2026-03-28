package com.example.kitsuneApi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kitsuneApi.model.AnimeProgress;

@Repository
public interface AnimeRepository extends JpaRepository<AnimeProgress, Long> {
    List<AnimeProgress> findByUserId(String userId);

    Optional<AnimeProgress> findByUserIdAndAnimeId(String userId, String animeId);
}