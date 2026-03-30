import '../styles/AnimeCard.css';

export interface AnimeCardData {
  id: string;
  title: string;
  image: string;
  japaneseTitle?: string;
  type?: string;
  sub: number;
  dub: number;
  episodes: number;
  results?: string;
}

const AnimeCard = ({ item }: { item: AnimeCardData }) => {
  return (
    <div className="anime-card" key={item.id}>
      <div className="poster-wrapper">
        <img src={item.image} alt={item.title} loading="lazy" />
        
        <div className="card-overlay">
          <div className="play-button">
            <svg viewBox="0 0 24 24" fill="currentColor">
              <path d="M8 5v14l11-7z" />
            </svg>
          </div>
        </div>

        <div className="badge-group">
          {item.sub > 0 && <span className="badge sub">SUB {item.sub}</span>}
          {item.dub > 0 && <span className="badge dub">DUB {item.dub}</span>}
        </div>
        
        <div className="episode-tag">
          {item.episodes} EP
        </div>
      </div>

      <div className="info-section">
        <div className="meta-top">
          <span className="type-label">{item.type || 'TV'}</span>
        </div>
        <h3 className="main-title">{item.title}</h3>
        {item.japaneseTitle && <p className="jp-title">{item.japaneseTitle}</p>}
      </div>
    </div>
  );
};

export default AnimeCard;