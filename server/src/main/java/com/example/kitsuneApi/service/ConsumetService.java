package com.example.kitsuneApi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ConsumetService {

    private final WebClient webClient;

    public ConsumetService(WebClient.Builder builder,
                           @Value("${consumet.api.url}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    public Mono<String> searchAnime(String query) {
        return this.webClient.get()
                .uri("/anime/animekai/{query}", query)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> {
                    System.out.println("AnimeKai is down, changing to AnimePahe...");
                    return this.webClient.get()
                            .uri("/anime/animepahe/{query}", query)
                            .retrieve()
                            .bodyToMono(String.class);
                });
    }

    public Mono<String> getAnimeInfo(String id) {
        return this.webClient.get()
                .uri("/anime/animekai/info/{id}", id)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> this.webClient.get()
                        .uri("/anime/animepahe/info/{id}", id)
                        .retrieve()
                        .bodyToMono(String.class));
    }

    public Mono<String> getWatchLinks(String episodeId) {
        return this.webClient.get()
                .uri("/anime/animekai/watch/{episodeId}", episodeId)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> this.webClient.get()
                        .uri("/anime/animepahe/watch/{episodeId}", episodeId)
                        .retrieve()
                        .bodyToMono(String.class));
    }
}
