import { useState } from "react";
import api from "../services/api";

interface MediaItem {
  id: string;
  title: {
    userPreferred: string;
    english?: string;
    romaji?: string;
  };
  image: string;
  rating?: number;
  popularity?: number;
  type?: string;
  releaseDate?: string | number;
  description?: string;
}

const Home = () => {
  const [results, setResults] = useState<MediaItem[]>([]);
  const [search, setSearch] = useState<string>("");

  const searchAnime = async (query: string) => {
    try {
      const response = await api.get(`/anime/search?query=${query}`);
      const data =
        typeof response.data === "string"
          ? JSON.parse(response.data)
          : response.data;

      if (data && data.results) {
        setResults(data.results);
        console.log("Sökresultat:", data.results);
      } else {
        setResults([]);
      }
    } catch (err) {
      console.error("Sökfel", err);
      setResults([]);
    }
  };

  return (
    <div className="container">
      <section className="hero">
        <h1>
          Kitsune<span>.</span>
        </h1>
        <form className="search-bar" onSubmit={(e) => e.preventDefault()}>
          <input
            type="text"
            placeholder="Search anime..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === "Enter") {
                e.preventDefault();
                searchAnime(search);
              }
            }}
          />
          <button type="submit" style={{ display: "none" }}>
            Search
          </button>
        </form>
      </section>

      <main>
        <h2 style={{ color: "var(--text-h)", marginBottom: "20px" }}>
          Trending
        </h2>
        <div className="grid">
          {results
            .slice()
            .sort((a, b) => {
              const ratingA = a.rating || 0;
              const ratingB = b.rating || 0;
              if (ratingB !== ratingA) return ratingB - ratingA;

              const yearA = Number(a.releaseDate) || 0;
              const yearB = Number(b.releaseDate) || 0;
              if (yearB !== yearA) return yearB - yearA;

              const popA = a.popularity || 0;
              const popB = b.popularity || 0;
              return popB - popA;
            })
            .map((item) => (
              <div key={item.id} className="card">
                <img
                  src={item.image}
                  alt={item.title.userPreferred || item.title.english}
                />
                <div className="card-info">
                  <h3>
                    {item.title.userPreferred ||
                      item.title.english ||
                      item.title.romaji}
                  </h3>

                  <div className="card-meta">
                    <span className="type-badge">{item.type}</span>
                    {item.rating && (
                      <span className="rating">⭐ {item.rating}%</span>
                    )}
                  </div>
                </div>
              </div>
            ))}
        </div>
      </main>
    </div>
  );
};

export default Home;
