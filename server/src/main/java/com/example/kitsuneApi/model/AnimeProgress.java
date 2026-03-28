package com.example.kitsuneApi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "anime_progress")
public class AnimeProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String animeId;
    private String title;
    private Integer lastEpisode;
    private String status;
}

