package com.example.kitsuneApi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class ConsumetService {

    private final WebClient webClient;

    @Value("${consumet.path.anime}")
    private String animePath;

    @Value("${consumet.path.anilist}")
    private String anilist;

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
                .uri(uriBuilder -> uriBuilder
                        .path(animePath + "/info")
                        .queryParam("id", id)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .bodyToMono(String.class)
                .onErrorResume(e -> Mono.empty());
    }

    public Mono<String> getWatchLinks(String episodeId, String server, Boolean dub) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(animePath + "/watch/" + episodeId)
                        .queryParam("server", server != null ? server : "vidstreaming")
                        .queryParam("dub", dub != null ? dub : false)
                        .build())
                .retrieve()
                .onStatus(status -> status.is5xxServerError(), response -> Mono.empty())
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
}
