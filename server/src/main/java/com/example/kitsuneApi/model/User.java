package com.example.kitsuneApi.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;
    private String bio;

    private String profilePicture;
    private Integer points = 0;

    @Column(nullable = false)
    private String role = "ROLE_USER";

    @ManyToMany
    @JoinTable(name = "user_favorites", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "media_id"))
    private Set<MediaItem> favorites = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_watch_later")
    private Set<MediaItem> watchLater = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_reading_list")
    private Set<MediaItem> readingList = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_completed")
    private Set<MediaItem> completed = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "user_search_history", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "search_query")
    private List<String> searchHistory = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "user_view_history", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "media_id"))
    @OrderColumn(name = "view_order")
    private List<MediaItem> viewHistory = new ArrayList<>();

    public int getLevel() {
        return (this.points / 100) + 1;
    }

    public int getXpToNextLevel() {
        return 100 - (this.points % 100);
    }

    public User() {
    }

    public User(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
