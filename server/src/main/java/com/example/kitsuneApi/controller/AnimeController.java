package com.example.kitsuneApi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.kitsuneApi.service.ConsumetService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("${properties.api.anime}")
@CrossOrigin("${frontend.api.url}")
public class AnimeController {

    private final ConsumetService consumetService;

    public AnimeController(ConsumetService consumetService) {
        this.consumetService = consumetService;
    }

    @GetMapping("")
    public String getStatus() {
        return "Kitsune Anime API is online";
    }

    @GetMapping("/search")
    public Mono<ResponseEntity<String>> search(@RequestParam String query) {
        return consumetService.searchAnime(query)
                .map(json -> ResponseEntity.ok().body(json))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorReturn(ResponseEntity.status(500).build());
    }

    @GetMapping("/info/{id}")
    public Mono<ResponseEntity<String>> getInfo(@PathVariable String id) {
        return consumetService.getAnimeInfo(id)
                .map(json -> ResponseEntity.ok().body(json))
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorReturn(ResponseEntity.status(500).build());
    }

}
