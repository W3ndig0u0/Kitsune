import { useMemo, useState } from "react";
import type { AnimeCardData } from "../components/AnimeCard";
import AnimeCard from "../components/AnimeCard";
import { animeService } from "../services/AnimeApi";
import '../styles/Search.css';

const Search = () => {
  const [results, setResults] = useState<AnimeCardData[]>([]);
  const [search, setSearch] = useState("");
  const [lastQuery, setLastQuery] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const handleSearch = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const query = search.trim();
    if (query.length < 3) return setError("At least 3 letters to search 🦊");

    setError(null);
    setIsLoading(true);
    setLastQuery(query);

    try {
      const response = await animeService.search(query);
      const data = response.data;
      setResults(data?.results || []);
    } catch (err) {
      setError("Could not fetch result 🦊" + (err as Error).message);
      setResults([]);
    } finally {
      setIsLoading(false);
    }
  };

  const sortedResults = useMemo(() => 
    [...results].sort((a, b) => (b.episodes || 0) - (a.episodes || 0))
  , [results]);

  return (
    <div className="search-page">
      <section className="hero">
        <h1>Kitsune<span>.</span></h1>
        <form className="search-bar" onSubmit={handleSearch}>
          <input
            placeholder="Search anime..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </form>
        {error && <p className="error-msg">{error}</p>}
      </section>

      {(isLoading || lastQuery) && (
        <section className="home-section">
          <div className="section-header">
            <div className="section-title-group">
              <h2>
                {isLoading ? "Searching..." : `Results for "${lastQuery}"`}
                <span>{isLoading ? "LOADING" : `${sortedResults.length} FOUND`}</span>
              </h2>
            </div>
          </div>

          {isLoading ? (
            <div className="loading-wrapper">
              <span className="loader-icon">🦊</span>
              <span className="loading-text">Hunting down titles...</span>
            </div>
          ) : (
            <div className="grid">
              {sortedResults.map((item) => (
                <AnimeCard key={item.id} item={item} />
              ))}
            </div>
          )}
        </section>
      )}
    </div>
  );
};

export default Search;