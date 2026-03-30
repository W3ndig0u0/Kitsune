import { useEffect, useState } from 'react';
import type { AnimeCardData } from '../components/AnimeCard';
import AnimeCard from '../components/AnimeCard';
import { animeService } from '../services/AnimeApi';
import Search from './Search';

const Home = () => {
    const [popular, setPopular] = useState<AnimeCardData[]>([]);
    const [recent, setRecent] = useState<AnimeCardData[]>([]); // Ny state för Recent
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchHomeData = async () => {
            try {
                const [popularRes, recentRes] = await Promise.all([
                    animeService.getPopular(),
                    animeService.getRecent()
                ]);

                setPopular(popularRes.data.results || []);
                setRecent(recentRes.data.results || []);
                
            } catch (err) {
                console.error("Could not fetch home data 🦊", err);
            } finally {
                setLoading(false);
            }
        };

        fetchHomeData();
    }, []);

    return (
        <div className="container">
            <Search />

            <section className="home-section">
                <h2>Senaste Avsnitten 🦊</h2>
                {loading ? (
                    <p>Laddar...</p>
                ) : (
                    <div className="grid">
                        {recent.map((anime: AnimeCardData) => (
                            <AnimeCard key={anime.id} item={anime} />
                        ))}
                    </div>
                )}
            </section>

            <section className="home-section">
                <h2>Populärt på Kitsune 🦊</h2>
                {loading ? (
                    <p>Laddar...</p>
                ) : (
                    <div className="grid">
                        {popular.map((anime: AnimeCardData) => (
                            <AnimeCard key={anime.id} item={anime} />
                        ))}
                    </div>
                )}
            </section>
        </div>
    );
};

export default Home;