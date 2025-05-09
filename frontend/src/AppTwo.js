import { useEffect, useState } from 'react';

import MovieResult from './components/MovieResult';
import UserResult from './components/UserResult';

import logo from './logo.svg';
import movieImage from './images/movie-list-image.png';
import './AppTwo.css';

import fullStar from './images/full-star.png';
import emptyStar from './images/empty-star.png';
import halfStars from './images/half-star.png';

const TMDB_API_KEY = 'YOUR_TMDB_API_KEY'; // Replace with your actual TMDb API key


function App() {
    const [view, setView] = useState('movies');
    const [data, setData] = useState([]);
    const [movies, setMovies] = useState([]);
  
    // (Include your original fetching logic here...)
  
    return (
      <div className="App-header">

        <div className="imageHeader">
            <img src={movieImage} alt="Movie Banner" className="movie-banner" />
        </div>
        <div className="buttonSelection">
          <button onClick={() => setView('movies')}>Movies</button>
          <button onClick={() => setView('users')}>Users</button>
          <button onClick={() => setView('popular')}>Popularity</button>
          <button onClick={() => setView('votebygender')}>Gender</button>
          <button onClick={() => setView('votebyagerange')}>Age Range</button>
        </div>
  
        {view === 'movies' && <MovieResult movies={movies} /* other props */ />}
        {view === 'users' && <UserResult data={data} />}

      </div>
    );
  }

export default App;
