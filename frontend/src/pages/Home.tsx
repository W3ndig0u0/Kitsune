import { useEffect, useState } from 'react';
import type { AnimeCardData } from '../components/AnimeCard';
import AnimeCard from '../components/AnimeCard';
import { animeService } from '../services/AnimeApi';
import '../styles/Home.css';
import Search from './Search';

interface HomeSectionProps {
  title: string;
  badge: string;
  data: AnimeCardData[];
  loading: boolean;
  isSlider?: boolean;
}

const HomeSection = ({ title, badge, data, loading, isSlider }: HomeSectionProps) => (
  <section className="home-section">
    <div className="section-header">
      <div className="section-title-group">
        <h2>{title}<span>{badge}</span></h2>
      </div>
    </div>

    {loading ? (
      <div className="loading-wrapper">
        <span className="loader-icon">🦊</span>
        <span className="loading-text">Fetching series data...</span>
      </div>
    ) : (
      <div className={isSlider ? "slider-container" : "grid"}>
        {data.map((anime) => (
          <div key={anime.id} className={isSlider ? "slider-item" : ""}>
            <AnimeCard item={anime} />
          </div>
        ))}
      </div>
    )}
  </section>
);

const Home = () => {
  const [data, setData] = useState<{ popular: AnimeCardData[]; recent: AnimeCardData[] }>({
    popular: [],
    recent: [],
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);

  useEffect(() => {
    const fetchHomeData = async () => {
      try {
        const [popularRes, recentRes] = await Promise.all([
          animeService.getPopular(),
          animeService.getRecent()
        ]);

        setData({
          popular: popularRes.data.results || [],
          recent: recentRes.data.results || [],
        });
      } catch (err) {
        console.error("Could not fetch home data", err);
        setError(true);
      } finally {
        setLoading(false);
      }
    };

    fetchHomeData();
  }, []);

  if (error) return <div className="error-msg">Failed to load content. Please try again later.</div>;
  return (
    <div className="home-page">
      <div className="container">
        <Search />

        <HomeSection 
          title="Recent Episodes" 
          badge="RECENT" 
          data={data.recent} 
          loading={loading} 
          isSlider={true} 
        />

        <HomeSection 
          title="Popular on Kitsune" 
          badge="TRENDING" 
          data={data.popular} 
          loading={loading} 
        />
      </div>
    </div>
  );
};

export default Home;