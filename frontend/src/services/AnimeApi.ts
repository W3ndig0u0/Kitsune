import type { AnimeCardData } from "../components/AnimeCard";
import api from "./api";

export interface SearchResponse {
  results: AnimeCardData[];
  currentPage?: number;
  hasNextPage?: boolean;
}

export interface StreamSource {
  url: string;
  quality: string;
  isM3U8: boolean;
}

export interface StreamResponse {
  headers: {
    Referer: string;
    "User-Agent": string;
  };
  sources: StreamSource[];
  download: string;
}

export const animeService = {
    getPopular: () => api.get<SearchResponse>("/anime/popular"),
    getRecent: () => api.get<SearchResponse>("/anime/recent"),
    search: (query: string) => api.get<SearchResponse>(`/anime/search?query=${query}`),
    getInfo: (id: string) => api.get(`/anime/info/${id}`),
    getStream: (episodeId: string, server: string = "vidstreaming") => {
      return api.get(`/anime/watch/${episodeId}`, {
        params: { server }
      });
    },
    logView: (id: string) => api.post("/api/anime/log-view", { id })
};