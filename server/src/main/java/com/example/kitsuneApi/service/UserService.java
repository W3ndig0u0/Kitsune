package com.example.kitsuneApi.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Viktigt för update/delete

import com.example.kitsuneApi.model.MediaItem;
import com.example.kitsuneApi.model.User;
import com.example.kitsuneApi.model.UserDto;
import com.example.kitsuneApi.model.UserProgress;
import com.example.kitsuneApi.model.UserProgressDto;
import com.example.kitsuneApi.repository.MediaRepository;
import com.example.kitsuneApi.repository.UserProgressRepository;
import com.example.kitsuneApi.repository.UserRepository;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;
    private final UserProgressRepository userProgressRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, MediaRepository mediaRepository,
            UserProgressRepository userProgressRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mediaRepository = mediaRepository;
        this.userProgressRepository = userProgressRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto registerUser(UserDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username '" + dto.getUsername() + "' is already taken");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        if (userRepository.count() == 0) {
            user.setRole("ROLE_ADMIN");
        } else {
            user.setRole("ROLE_USER");
        }

        user.setPoints(0);
        user.setBio("System Administrator");

        user.setPoints(dto.getPoints() != null ? dto.getPoints() : 0);
        user.setBio(dto.getBio() != null ? dto.getBio() : "New explorer at Kitsune!");
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    @Transactional(readOnly = true)
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Not found"));
        return convertToDto(user);
    }

    public UserDto updateUser(Long id, UserDto updatedDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found"));

        user.setUsername(updatedDto.getUsername());
        user.setEmail(updatedDto.getEmail());
        user.setPassword(passwordEncoder.encode(updatedDto.getPassword()));

        userRepository.save(user);
        return convertToDto(user);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User ID " + id + " not found"));
        return convertToDto(user);
    }

    @Transactional(readOnly = true)
    public boolean authenticateUser(String username, String password) {
        return userRepository.findByUsername(username)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User " + username + " not found"));
        userRepository.delete(user);
    }

    public void addMediaToList(String username, String consumetId, MediaItem mediaDto, String listType) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User " + username + " not found"));

        MediaItem media = mediaRepository.findByConsumetId(consumetId)
                .orElseGet(() -> {
                    MediaItem newMedia = new MediaItem();
                    newMedia.setConsumetId(consumetId);
                    newMedia.setTitle(mediaDto.getTitle());
                    newMedia.setImage(mediaDto.getImage());
                    newMedia.setType(mediaDto.getType());
                    newMedia.setProvider(mediaDto.getProvider());
                    return mediaRepository.save(newMedia);
                });

        switch (listType.toUpperCase()) {
            case "FAVORITE" -> user.getFavorites().add(media);
            case "WATCH_LATER" -> user.getWatchLater().add(media);
            case "READING_LIST" -> user.getReadingList().add(media);
            default -> throw new IllegalArgumentException("Invalid list type: " + listType);
        }

        user.setPoints(user.getPoints() + 10);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserProgressDto getMediaProgress(String username, String consumetId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return userProgressRepository.findByUserIdAndMediaConsumetId(user.getId(), consumetId)
                .map(pro -> new UserProgressDto(
                        pro.getMedia().getConsumetId(),
                        pro.getLastEpisodeId(),
                        pro.getLastTimeInSeconds()))
                .orElse(new UserProgressDto(consumetId, null, 0));
    }

    public void saveProgress(String username, UserProgress dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String consumetId = dto.getMedia().getConsumetId();
        MediaItem media = getOrCreateMedia(dto.getMedia());

        UserProgress progress = userProgressRepository.findByUserIdAndMediaConsumetId(user.getId(), consumetId)
                .orElseGet(() -> {
                    UserProgress newProgress = new UserProgress();
                    newProgress.setUser(user);
                    newProgress.setMedia(media);
                    return newProgress;
                });

        progress.setLastEpisodeId(dto.getLastEpisodeId());
        progress.setLastTimeInSeconds(dto.getLastTimeInSeconds());
        progress.setLastWatched(java.time.LocalDateTime.now());

        user.setPoints(user.getPoints() + 2);

        userProgressRepository.save(progress);
        userRepository.save(user);
    }

    public void markAsCompleted(String username, String consumetId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        MediaItem media = mediaRepository.findByConsumetId(consumetId)
                .orElseThrow(() -> new IllegalArgumentException("Media not found in Database"));

        user.getCompleted().add(media);
        user.getWatchLater().remove(media);
        user.getReadingList().remove(media);
        user.setPoints(user.getPoints() + 50);
        userRepository.save(user);
    }

    public void addToViewHistory(String username, MediaItem mediaDto) {
        User user = userRepository.findByUsername(username).orElseThrow();
        MediaItem media = getOrCreateMedia(mediaDto);
        user.getViewHistory().remove(media);

        user.getViewHistory().add(0, media);

        if (user.getViewHistory().size() > 10) {
            user.setViewHistory(new ArrayList<>(user.getViewHistory().subList(0, 10)));
        }

        userRepository.save(user);
    }

    private MediaItem getOrCreateMedia(MediaItem dto) {
        return mediaRepository.findByConsumetId(dto.getConsumetId())
                .orElseGet(() -> {
                    MediaItem newMedia = new MediaItem();
                    newMedia.setConsumetId(dto.getConsumetId());
                    newMedia.setTitle(dto.getTitle());
                    newMedia.setImage(dto.getImage());
                    newMedia.setType(dto.getType());
                    newMedia.setProvider(dto.getProvider());
                    return mediaRepository.save(newMedia);
                });
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setBio(user.getBio());
        dto.setProfilePicture(user.getProfilePicture());
        dto.setPoints(user.getPoints());

        dto.setLevel((user.getPoints() / 100) + 1);
        dto.setXpToNextLevel(100 - (user.getPoints() % 100));
        dto.setRole(user.getRole());

        dto.setPassword(null);

        return dto;
    }

    public Map<String, Object> getFullUserProfile(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        UserDto userDto = convertToDto(user);
        List<UserProgress> recentProgress = userProgressRepository.findByUserId(user.getId());
        Map<String, Object> response = new HashMap<>();
        response.put("user", userDto);
        response.put("favorites", user.getFavorites());
        response.put("watchLater", user.getWatchLater());
        response.put("completed", user.getCompleted());
        response.put("readingList", user.getReadingList());
        response.put("recentProgress", recentProgress);
        response.put("searchHistory", user.getSearchHistory());
        response.put("viewHistory", user.getViewHistory());
        return response;
    }

    public void addSearchQuery(String username, String query) {
        User user = userRepository.findByUsername(username).orElseThrow();
        user.getSearchHistory().remove(query);
        user.getSearchHistory().add(0, query);
        if (user.getSearchHistory().size() > 10) {
            user.setSearchHistory(user.getSearchHistory().subList(0, 10));
        }

        userRepository.save(user);
    }

}
