import { Route, Routes } from 'react-router-dom'
import Home from './pages/Home'
import './styles/App.css'

function App() {
  return (
    <div className="app-container">
      <nav className="main-nav">
        <div className="nav-content">
          <a href="/" className="logo">Kitsune</a>
          <div className="nav-links">
            <a href="/search">Search</a>
            <a href="/login">Login</a>
          </div>
        </div>
      </nav>

      <Routes>
        <Route path="/" element={<Home />} />
        {/* <Route path="/login" element={<Login />} /> */}
        {/* <Route path="/profile" element={<Profile />} /> */}
        
        <Route path="*" element={<h1>404 - Page Not Found</h1>} />
      </Routes>
      
      <footer className="footer">
        <p>&copy; 2026 Kitsune Anime</p>
      </footer>
    </div>
  )
}

export default App
