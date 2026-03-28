package com.example.kitsuneApi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kitsuneApi.model.MediaItem;
import com.example.kitsuneApi.model.UserProgress;
import com.example.kitsuneApi.model.UserProgressDto;
import com.example.kitsuneApi.service.UserService;

@RestController
@RequestMapping("${properties.api.media}")
public class MediaController {

    private final UserService userService;

    public MediaController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/progress")
    public ResponseEntity<Void> updateProgress(@RequestBody UserProgress dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.saveProgress(username, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/progress/{consumentId}")
    public ResponseEntity<UserProgressDto> getProgress(@PathVariable String consumetId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userService.getMediaProgress(username, consumetId));
    }

    @PostMapping("/favo/{consumetId}")
    public ResponseEntity<String> postMethodName(@PathVariable String consumetId, @RequestBody MediaItem mediaItem) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.addMediaToList(username, consumetId, mediaItem, "FAVORITE");
        return ResponseEntity.ok().body("Anime added to favorites for user: " + username);
    }

    @PostMapping("/complete/{consumetId}")
    public ResponseEntity<String> completeMedia(@PathVariable String consumetId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.markAsCompleted(username, consumetId);
        return ResponseEntity.ok("Well done! You've earned points for completing the media!");
    }

}
