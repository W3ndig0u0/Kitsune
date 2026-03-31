import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { animeService } from '../services/AnimeApi';
import '../styles/StreamingSite.css';

const AnimeStream = () => {
  const { id } = useParams<{ id: string }>();
  const [anime, setAnime] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [streamingUrl, setStreamingUrl] = useState<string | null>(null);
  const [isBuffering, setIsBuffering] = useState(false);
  const [embedUrl, setEmbedUrl] = useState<string | null>(null);
  const handleWatchEpisode = async (episodeId: string) => {
    setIsBuffering(true);
    try {
      const res = await animeService.getStream(episodeId);
      console.log(res);
      const data = typeof res.data === 'string' ? JSON.parse(res.data) : res.data;
      if (data.sources && data.sources.length > 0) {
        const primarySource = data.sources[0].url;
        setStreamingUrl(primarySource);
        setEmbedUrl(null);
        window.scrollTo({ top: 0, behavior: 'smooth' });
      }
    } catch (err) {
      alert("Could not load video stream 🦊");
    } finally {
      setIsBuffering(false);
    }
  };

  useEffect(() => {
    const fetchDetails = async () => {
      if (!id) return;
      setLoading(true);
      setError(null);
      
      try {
        const res = await animeService.getInfo(id);
        
        const data = typeof res.data === 'string' ? JSON.parse(res.data) : res.data;
        
        if (!data || data.error) {
          setError(data?.error || "Anime not found on this provider 🦊");
        } else {
          setAnime(data);
        }
      } catch (err) {
        console.error("Error fetching anime info", err);
        setError("Could not connect to Kitsune API.");
      } finally {
        setLoading(false);
      }
    };
    
    fetchDetails();
  }, [id]);

  if (loading) return (
    <div className="loading-wrapper">
      <span className="loader-icon">🦊</span>
      <span className="loading-text">Fetching details...</span>
    </div>
  );

  if (error || !anime) return (
    <div className="container">
      <div className="error-msg">{error || "Anime not found."}</div>
    </div>
  );

  return (
<div className="details-page container">
      {streamingUrl && (
        <section className="video-player-container">
          <div className="player-wrapper">
            <video controls autoPlay src={streamingUrl} className="main-video" />
          </div>
          <button className="close-player" onClick={() => setStreamingUrl(null)}>Close Player</button>
        </section>
      )}

      {embedUrl && (
        <div className="video-player-wrapper">
          <iframe
            src={embedUrl}
            className="main-iframe"
            allowFullScreen
            scrolling="no"
            allow="autoplay; encrypted-media"
            title="Anime Player"
          />
        </div>
      )}
      <div className="details-hero">
        <div className="details-backdrop" style={{ backgroundImage: `url(${anime.image})` }} />
        <div className="details-content">
          <img src={anime.image} alt={anime.title} className="details-poster" />
          <div className="details-text">
            <div className="section-title-group">
              <h1>{anime.title} <span>{anime.type || 'TV'}</span></h1>
            </div>
            <p className="description" dangerouslySetInnerHTML={{ __html: anime.description }} />
            
            <div className="meta-grid">
              <div className="meta-item"><strong>Status</strong> <span>{anime.status}</span></div>
              <div className="meta-item"><strong>Total EP</strong> <span>{anime.totalEpisodes}</span></div>
              <div className="meta-item"><strong>Sub/Dub</strong> <span>{anime.subOrDub?.toUpperCase()}</span></div>
            </div>
          </div>
        </div>
      </div>

      <section className="home-section">
        <div className="section-header">
          <div className="section-title-group">
        </div>
        
          <h2>Episodes <span>{isBuffering ? 'FETCHING LINK...' : 'STREAM'}</span></h2>
        </div>
        <div className="episode-grid">
          {anime.episodes?.map((ep: any) => (
            <button 
              key={ep.id} 
              onClick={() => handleWatchEpisode(ep.id)} 
              className={`episode-card ${isBuffering ? 'disabled' : ''}`}
            >
              <div className="ep-number">EP {ep.number}</div>
              <div className="ep-play">Watch Now</div>
            </button>
          ))}
        </div>
      </section>      
    </div>
  );
};

export default AnimeStream;