package com.example.kitsuneApi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.kitsuneApi.model.MediaItem;
import com.example.kitsuneApi.service.ConsumetService;
import com.example.kitsuneApi.service.UserService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("${properties.api.anime}")
public class AnimeController {

    private final ConsumetService consumetService;
    private final UserService userService;

    public AnimeController(ConsumetService consumetService, UserService userService) {
        this.consumetService = consumetService;
        this.userService = userService;
    }

    @GetMapping("")
    public String getStatus() {
        return "Kitsune Anime API is online";
    }

    @GetMapping(value = "/search", produces = "application/json")
    public Mono<ResponseEntity<String>> search(@RequestParam String query) {
        String authName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!authName.equals("anonymousUser")) {
            userService.addSearchQuery(authName, query);
        }

        return consumetService.searchAnime(query)
                .map(json -> ResponseEntity.ok().body(json))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/popular", produces = "application/json")
    public Mono<ResponseEntity<String>> getPopular() {
        return consumetService.getPopularAnime()
                .map(json -> ResponseEntity.ok().body(json));
    }

    @GetMapping(value = "/recent", produces = "application/json")
    public Mono<ResponseEntity<String>> getRecent() {
        return consumetService.getRecentEpisodes()
                .map(json -> ResponseEntity.ok().body(json));
    }

    @GetMapping("/info/{id}")
    public Mono<ResponseEntity<String>> getInfo(@PathVariable String id) {
        return consumetService.getAnimeInfo(id)
                .map(json -> ResponseEntity.ok().body(json))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorReturn(ResponseEntity.status(500).build());
    }

    @PostMapping(value = "/log-view", produces = "application/json")
    public ResponseEntity<Void> logView(@RequestBody MediaItem item) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!username.equals("anonymousUser")) {
            userService.addToViewHistory(username, item);
        }
        return ResponseEntity.ok().build();
    }

}
