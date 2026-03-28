package com.example.kitsuneApi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.kitsuneApi.model.MediaItem;

public interface MediaRepository extends JpaRepository<MediaItem, Long> {
    Optional<MediaItem> findByConsumetId(String consumetId);

}
