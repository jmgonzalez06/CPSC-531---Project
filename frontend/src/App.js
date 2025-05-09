import { useEffect, useState } from 'react';
import logo from './logo.svg';
import movieImage from './images/movie-list-image.png';
import './App.css';

import fullStar from './images/full-star.png';
import emptyStar from './images/empty-star.png';
import halfStars from './images/half-star.png';

const TMDB_API_KEY = 'YOUR_TMDB_API_KEY'; // Replace with your actual TMDb API key


function App() {

  //Toggle between outputs
  const [view, setView] = useState('movies'); // 'movies' or 'users'
  const [data, setData] = useState([]);

  //this will be used to find the movie list through the search feature
  const [searchTerm, setSearchTerm] = useState('');
  const [searchResults, setSearchResults] = useState([]);

  //We're going to search the movie by genre for the search menu:
  const [selectedGenre, setSelectedGenre] = useState('');
  //We're also going to set the genre from the movies:
  const [genres, setGenres] = useState([]);

  //This is for the movies to be displayed
  const [movies, setMovies] = useState([]);
  //When we select the movie, it'll use this useState
  const [selectedMovie, setSelectedMovie] = useState(null);

  // Helper function to display stars based on rating.  This is more for an effect
  const renderStars = (rating) => {
    // Round to the nearest 0.5 to account for half-stars
    const fullStars = Math.floor(rating);
    const halfStar = rating % 1 >= 0.5 ? 1 : 0;
    const emptyStars = 5 - fullStars - halfStar;

    // Create the star representation using images
    const starImages = [];
    for (let i = 0; i < fullStars; i++) {
      starImages.push(<img key={`full-${i}`} src={fullStar} alt="full star" className="star" />);
    }
    if (halfStar) {
      starImages.push(<img key="half" src={halfStars} alt="half star" className="star" />);
    }
    for (let i = 0; i < emptyStars; i++) {
      starImages.push(<img key={`empty-${i}`} src={emptyStar} alt="empty star" className="star" />);
    }

    return starImages;
  };


  //We're extracting the information for the genre:
  



  //To be able to do the search enginer, we need to do the useState function.  We'll do this:
  const handleSearchChange = (e) => {
    const term = e.target.value;
    setSearchTerm(term);
  
    if (term.trim() === '') {
      setSearchResults([]);
    } else {
      const filtered = movies.filter(movie => {
        const matchesTitle = movie.title.toLowerCase().includes(term.toLowerCase());
        const matchesGenre = selectedGenre ? movie.genre === selectedGenre : true;
        return matchesTitle && matchesGenre;
      });
      setSearchResults(filtered);
    }
  };
  
  const handleSearchSelect = (movieId) => {
    const movie = movies.find(m => m.movieId === movieId);
    setSelectedMovie(movie);
    setSearchTerm('');
    setSearchResults([]);
  };


  //We're going to be using effects for this.  First, we need to retrieve the movie list

  // We're going to be using effects for this. First, we need to retrieve the movie list
  useEffect(() => {
    fetch('/output/final_movie_list.csv') // Fetch the file from the public folder
      .then(res => res.text())
      .then(text => {
        const lines = text.trim().split('\n');
        const headers = lines[0].replace(/"/g, '').split(',');
        const movieData = lines.slice(1).map(line => {
          const values = line.match(/(".*?"|[^",\s]+)(?=\s*,|\s*$)/g).map(v => v.replace(/^"|"$/g, ''));
          const movie = {};
          headers.forEach((header, i) => {
            movie[header] = header === 'avgRating' ? parseFloat(values[i]) : values[i];
          });
          return movie;
        });
  
        setMovies(movieData);
  
        // 🔥 Extract unique genres from the loaded movie data
        const uniqueGenres = [...new Set(movieData.map(movie => movie.genre))].sort();
        setGenres(uniqueGenres);
      })
      .catch(error => console.error('Error loading movie data:', error));
  }, []);
  // Handle movie selection from the dropdown
  const handleSelectChange = (event) => {
    const movieId = event.target.value;
    const movie = movies.find(m => m.movieId === movieId);
    setSelectedMovie(movie);  // Set the selected movie to state
  };

  


  useEffect(() => {
    const fileToLoad = view === 'movies' ? '/movie_results.txt' : '/user_results.txt';
  
    fetch(fileToLoad)
      .then(res => res.text())
      .then(text => {
        const lines = text.trim().split('\n');
  
        if (view === 'movies') {
          const headers = lines[0].replace(/"/g, '').split(',');
          const movieData = lines.slice(1).map(line => {
            const values = line.match(/(".*?"|[^",\s]+)(?=\s*,|\s*$)/g).map(v => v.replace(/^"|"$/g, ''));
            const movie = {};
            headers.forEach((header, i) => {
              movie[header] = header === 'avgRating' ? parseFloat(values[i]) : values[i];
            });
            return movie;
          });
          //I'm not sure if this works, but we'll try
          const uniqueGenres = [...new Set(movieData.map(movie => movie.genre))].sort();
          setGenres(uniqueGenres);
          //End of the new linie commands
          setData(movieData);
        } else {
          const headers = ['userId', 'age', 'gender', 'occupation', 'zipCode'];
          const userData = lines.map(line => {
            const values = line.split('|');
            const user = {};
            headers.forEach((header, i) => {
              user[header] = values[i];
            });
            return user;
          });
          setData(userData);
        }
      })
      .catch(error => console.error('Error loading data:', error));
  }, [view]);

  return (

  <div className="App">
    <header className="App-header">
      <img src={movieImage} className ="movieImage" />


      <div style={{ marginBottom: '20px' }}>
          <button onClick={() => setView('movies')}>Show Movies</button>
          <button onClick={() => setView('users')}>Show Users</button>
      </div>



      {view === 'movies' && (
        <div className = 'movie-search-section'>
          <h3>Select a Movie:</h3>

          <div className = 'movie-search'>
            {/* Search bar */}
            <div style={{ marginTop: '20px' }}>
              <input
                type="text"
                placeholder="Search for a movie..."
                value={searchTerm}
                onChange={handleSearchChange}
              />
              {searchResults.length > 0 && (
              <ul style={{ listStyleType: 'none', padding: 0 }}>
                {searchResults.map(movie => (
                  <li key={movie.movieId}>
                    <button onClick={() => handleSearchSelect(movie.movieId)}>
                      {movie.title}
                    </button>
                  </li>
                ))}
              </ul>
              )}
            </div>
            {/* Dropdown */}
            <div style={{ marginTop: '10px' }}>
            <label>Filter by Genre: </label>
            <select value={selectedGenre} onChange={e => setSelectedGenre(e.target.value)}>
              <option value="">All Genres</option>
              {genres.map((genre, idx) => (
                <option key={idx} value={genre}>{genre}</option>
              ))}
            </select>
            </div>


          </div>


          {/* Movie Details */}
          {selectedMovie && (
            <div style={{ marginTop: '20px' }}>
              <h3>Title: {selectedMovie.title}</h3>
              <p><strong>Genre:</strong> {selectedMovie.genre}</p>
              <p><strong>Avg Rating:</strong> {renderStars(selectedMovie.avgRating)}</p>
              <p><strong>Votes:</strong> {selectedMovie.numRatings}</p>
              <p><strong>Popular:</strong> {selectedMovie.isPopular}</p>
            </div>
          )}
        </div>
      )}

        {view === 'users' && movies.length > 0 && (
          <div>
            <h3>Users:</h3>
            <ul>
              {movies.map((user, idx) => (
                <li key={idx}>{JSON.stringify(user)}</li>
              ))}
            </ul>
          </div>
        )}

    </header>
  </div>
  );
}

export default App;
