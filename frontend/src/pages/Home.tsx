import { useState } from "react";
import api from "../services/api";

interface MediaItem {
  consumetId: string;
  title: string;
  image: string;
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
          {results.map((item) => (
            <div key={item.consumetId} className="card">
              <img src={item.image} alt={item.title} />
              <h3>{item.title}</h3>
            </div>
          ))}
        </div>
      </main>
    </div>
  );
};

export default Home;
