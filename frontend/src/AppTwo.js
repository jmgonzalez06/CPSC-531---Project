import { useEffect, useState } from 'react';

import MovieResult from './components/MovieResult';
import UserResult from './components/UserResult';
import AvgRatingByOccupation from './components/AvgRatingByOccupation';
import GenresByAge from './components/GenresByAge';
import TopGenresByGender from './components/TopGenresByGender';
import TopGenresByOccupation from './components/TopGenresByOccupation';

import movieImage from './images/movie-list-image.png';
import './AppTwo.css';

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
          <button onClick={() => setView('ratingsOccupation')}>Ratings By Occupation</button>
          <button onClick={() => setView('genresByAge')}>Genres By Age</button>
          <button onClick={() => setView('topGenresByGender')}>Genres by Gender</button>
          <button onClick={() => setView('genresByOccupation')}>Genres By Occupation</button>
        </div>
  
        {view === 'movies' && <MovieResult movies={movies} /* other props */ />}
        {view === 'ratingsOccupation' && <AvgRatingByOccupation data={data} />}
        {view === 'genresByAge' && <GenresByAge data={data} />}
        {view === 'topGenresByGender' && <TopGenresByGender data={data} />}
        {view === 'genresByOccupation' && <TopGenresByOccupation data={data} />}

      </div>
    );
  }

export default App;
