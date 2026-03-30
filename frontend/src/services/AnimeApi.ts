import type { AnimeCardData } from "../components/AnimeCard";
import api from "./api";

export interface SearchResponse {
  results: AnimeCardData[];
  currentPage?: number;
  hasNextPage?: boolean;
}


export const animeService = {
    getPopular: () => api.get<SearchResponse>("/anime/popular"),
    getRecent: () => api.get<SearchResponse>("/anime/recent"),
    search: (query: string) => api.get<SearchResponse>(`/anime/search?query=${query}`),
    getInfo: (id: string) => api.get(`/anime/info/${id}`),
    logView: (id: string) => api.post("/api/anime/log-view", { id })
};