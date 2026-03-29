import { useMemo, useState } from "react";
import "../App.css";
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
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const searchAnime = async (query: string) => {
    try {
      const response = await api.get(`/anime/search?query=${query}`);
      const data =
        typeof response.data === "string"
          ? JSON.parse(response.data)
          : response.data;
      console.log("Söker efter:", response.data);

      if (data && data.results) {
        setResults(data.results);
      } else {
        setResults([]);
      }
    } catch (err) {
      console.error("Sökfel", err);
      setResults([]);
    }
  };

  const handleSearch = async (e?: React.FormEvent) => {
    e?.preventDefault();
    const query = search.trim();
    if (query.length < 6) {
      setError("Sökordet måste vara minst 6 tecken 🦊");
      return;
    }

    setError(null);
    setIsLoading(true);

    try {
      await searchAnime(query);
    } finally {
      setIsLoading(false);
    }
  };

  const sortedResults = useMemo(() => {
    return [...results].sort((a, b) => {
      const ratingB = b.rating || 0;
      const ratingA = a.rating || 0;
      if (ratingB !== ratingA) return ratingB - ratingA;

      const yearB = Number(b.releaseDate) || 0;
      const yearA = Number(a.releaseDate) || 0;
      if (yearB !== yearA) return yearB - yearA;

      return (b.popularity || 0) - (a.popularity || 0);
    });
  }, [results]);

  return (
    <div className="container">
      <section className="hero">
        <h1>
          Kitsune<span>.</span>
        </h1>

        <form className="search-bar" onSubmit={handleSearch}>
          <input
            type="text"
            placeholder="Search anime..."
            value={search}
            onChange={(e) => {
              setSearch(e.target.value);
              if (e.target.value.length >= 6) setError(null);
            }}
          />
          <button type="submit" style={{ display: "none" }}>
            Search
          </button>
        </form>

        {error && (
          <p style={{ color: "var(--accent)", marginTop: "10px" }}>{error}</p>
        )}
      </section>

      <main>
        <h2 style={{ color: "var(--text-h)", marginBottom: "20px" }}>
          {isLoading ? "Searching..." : "Results"}
        </h2>

        <div className="grid">
          {sortedResults.map((item) => (
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
