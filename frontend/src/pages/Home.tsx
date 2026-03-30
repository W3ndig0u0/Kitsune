import { useEffect, useState } from 'react';
import type { AnimeCardData } from '../components/AnimeCard';
import AnimeCard from '../components/AnimeCard';
import { animeService } from '../services/AnimeApi';
import Search from './Search';

const Home = () => {
    const [popular, setPopular] = useState<AnimeCardData[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchPopular = async () => {
            try {
                const response = await animeService.getPopular();
                setPopular(response.data);
            } catch (err) {
                console.error("Kunde inte hämta populär anime 🦊", err);
            } finally {
                setLoading(false);
            }
        };
        fetchPopular();
    }, []);

    return (
        <div className="container">
          <Search />
            <section className="home-section">
                <h2>Populärt på Kitsune 🦊</h2>
                {loading ? (
                    <p>Laddar...</p>
                ) : (
                    <div className="grid">
                        {popular.map((anime) => (
                            <AnimeCard key={anime.id} item={anime} />
                        ))}
                    </div>
                )}
            </section>
        </div>
    );
};

export default Home;