package com.example.kitsuneApi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class ConsumetService {

    private final WebClient webClient;

    @Value("${consumet.path.anime}")
    private String animePath;

    @Value("${consumet.path.anikai}")
    private String anikaiPath;

    @Value("${consumet.path.manga}")
    private String mangaPath;

    public ConsumetService(WebClient.Builder builder, @Value("${consumet.api.url}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    public Mono<String> searchAnime(String query) {
        return this.webClient.get()
                .uri(animePath + "/{query}", query)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getAnimeInfo(String id) {
        return this.webClient.get()
                .uri(animePath + "/info/{id}", id)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getWatchLinks(String episodeId) {
        return this.webClient.get()
                .uri(animePath + "/watch/{episodeId}", episodeId)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getTrendingAnime() {
        return this.webClient.get()
                .uri(animePath + "/trending")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getPopularAnime() {
        return this.webClient.get()
                .uri(animePath + "/popular")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getRecentEpisodes() {
        return this.webClient.get()
                .uri(animePath + "/recent-episodes")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getRandomAnime() {
        return this.webClient.get()
                .uri(animePath + "/random-anime")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getAiringSchedule() {
        return this.webClient.get()
                .uri(animePath + "/airing-schedule")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> searchManga(String query) {
        return this.webClient.get()
                .uri(mangaPath + "/{query}", query)
                .retrieve()
                .bodyToMono(String.class);
    }
}
