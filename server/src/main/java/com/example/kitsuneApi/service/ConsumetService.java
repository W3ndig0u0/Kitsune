package com.example.kitsuneApi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ConsumetService {

    private final WebClient webClient;

    @Value("${consumet.provider.primary}")
    private String primaryProvider;

    @Value("${consumet.provider.fallback}")
    private String fallbackProvider;

    public ConsumetService(WebClient.Builder builder,
                           @Value("${consumet.api.url}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    public Mono<String> searchAnime(String query) {
        return this.webClient.get()
                .uri("/anime/{provider}/{query}", primaryProvider, query)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> {
                    return this.webClient.get()
                            .uri("/anime/{provider}/{query}", fallbackProvider, query)
                            .retrieve()
                            .bodyToMono(String.class);
                });
    }

    public Mono<String> getAnimeInfo(String id) {
        return this.webClient.get()
                .uri("/anime/{provider}/info/{id}", primaryProvider, id)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> this.webClient.get()
                        .uri("/anime/{provider}/info/{id}", fallbackProvider, id)
                        .retrieve()
                        .bodyToMono(String.class));
    }

    public Mono<String> getWatchLinks(String episodeId) {
        return this.webClient.get()
                .uri("/anime/{provider}/watch/{episodeId}", primaryProvider, episodeId)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> this.webClient.get()
                        .uri("/anime/{provider}/watch/{episodeId}", fallbackProvider, episodeId)
                        .retrieve()
                        .bodyToMono(String.class));
    }
}
