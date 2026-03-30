import { useMemo, useState } from "react";
import type { AnimeCardData } from "../components/AnimeCard";
import AnimeCard from "../components/AnimeCard";
import { animeService } from "../services/AnimeApi";
import '../styles/Search.css';

const Search = () => {
  const [results, setResults] = useState<AnimeCardData[]>([]);
  const [search, setSearch] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();
    const query = search.trim();
    if (query.length < 3) return setError("At least 3 letters to search 🦊");

    setError(null);
    setIsLoading(true);

    try {
      const response = await animeService.search(query);
      console.log("Search response:", response);
      const data = response.data;
      setResults(data?.results || []);
    } catch (err) {
      setError("Could not fetch result 🦊" + err);
      setResults([]);
    } finally {
      setIsLoading(false);
    }
  };

  const sortedResults = useMemo(() => 
    [...results].sort((a, b) => (b.episodes || 0) - (a.episodes || 0))
  , [results]);

  return (
    <div className="container">
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

      <main>
      <h2>
        {isLoading 
          ? "Searching..." 
          : search && (sortedResults.length > 0 
              ? `Results for "${search}"` 
              : `No results found for "${search}" 🦊`)
        }
      </h2>
        <div className="grid">
          {sortedResults.map((item) => (
            <AnimeCard key={item.id} item={item} />
          ))}
        </div>
      </main>
    </div>
  );
};

export default Search;