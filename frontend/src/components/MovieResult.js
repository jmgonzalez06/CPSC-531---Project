import React, { useState, useEffect } from 'react';
import fullStar from '../images/full-star.png';
import halfStar from '../images/half-star.png';
import emptyStar from '../images/empty-star.png';
import './MovieResult.css';

const MovieResult = () => {
  const [movies, setMovies] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [selectedGenre, setSelectedGenre] = useState('');
  const [genres, setGenres] = useState([]);
  const [selectedMovie, setSelectedMovie] = useState(null);

  useEffect(() => {
    fetch('../output/final_movie_list.csv')
      .then(res => res.text())
      .then(text => {
        const lines = text.trim().split('\n');
        const headers = lines[0].replace(/"/g, '').split(',');

        const movieData = lines.slice(1).map(line => {
          const values = line
            .split(/,(?=(?:(?:[^"]*"){2})*[^"]*$)/)
            .map(v => v.replace(/^"|"$/g, '').trim());

          const movie = {};
          headers.forEach((header, i) => {
            const cleanHeader = header.trim();
            const value = values[i]?.trim();

            if (cleanHeader === 'avgRating') {
              movie[cleanHeader] = parseFloat(value) || 0;
            } else if (cleanHeader === 'numRatings') {
              movie[cleanHeader] = parseInt(value, 10) || 0;
            } else if (cleanHeader === 'movieId') {
              movie[cleanHeader] = value.toString();
            } else if (cleanHeader === 'genre') {
              movie[cleanHeader] = value.split(',').map(g => g.trim());
            } else {
              movie[cleanHeader] = value;
            }
          });

          return movie;
        });

        setMovies(movieData);
        const allGenres = movieData.flatMap(movie => movie.genre);
setGenres([...new Set(allGenres)].sort());
      })
      .catch(err => console.error('Failed to load movie data:', err));
  }, []);

  const renderStars = (rating) => {
    const fullStars = Math.floor(rating);
    const decimal = rating - fullStars;
    const hasHalfStar = decimal >= 0.25 && decimal < 0.75;
    const roundedUp = decimal >= 0.75;
    const totalFullStars = roundedUp ? fullStars + 1 : fullStars;
    const emptyStars = 5 - totalFullStars - (hasHalfStar ? 1 : 0);

    const stars = [];

    for (let i = 0; i < totalFullStars; i++) {
      stars.push(<img key={`full-${i}`} src={fullStar} alt="Full Star" className="star" />);
    }

    if (hasHalfStar) {
      stars.push(<img key="half" src={halfStar} alt="Half Star" className="star" />);
    }

    for (let i = 0; i < emptyStars; i++) {
      stars.push(<img key={`empty-${i}`} src={emptyStar} alt="Empty Star" className="star" />);
    }

    return stars;
  };

  const handleSearchChange = (e) => {
    const term = e.target.value;
    setSearchTerm(term);
    if (!term.trim()) return setSearchResults([]);

    const filtered = movies.filter(movie => {
      const titleMatch = movie.title.toLowerCase().includes(term.toLowerCase());
      const genreMatch = selectedGenre ? movie.genre.includes(selectedGenre) : true;
      return titleMatch && genreMatch;
    });
    setSearchResults(filtered);
  };

  const handleSearchSelect = (movieId) => {
    const movie = movies.find(m => m.movieId === movieId.toString());
    setSelectedMovie(movie);
    setSearchTerm('');
    setSearchResults([]);
  };

  return (
    <div className="movie-result">
      <h2>Movie Results</h2>

      <div>
        <label className="wordGenre">Filter by Genre:</label>
        <select className="genre-select" value={selectedGenre} onChange={e => setSelectedGenre(e.target.value)}>
          <option value="">All Genres</option>
          {genres.map((g, idx) => (
            <option key={idx} value={g}>{g}</option>
          ))}
        </select>
      </div>

      <div style={{ position: 'relative', width: '300px' }}>
        <input
          type="text"
          placeholder="Search movies..."
          value={searchTerm}
          onChange={handleSearchChange}
          style={{ width: '100%', padding: '8px' }}
        />
        {searchResults.length > 0 && (
          <ul style={{
            listStyle: 'none',
            paddingLeft: 0,
            marginTop: '4px',
            maxHeight: '200px',
            overflowY: 'auto',
            border: '1px solid #ccc',
            borderRadius: '4px',
            backgroundColor: '#fff',
            position: 'absolute',
            width: '100%',
            zIndex: 1000
          }}>
            {searchResults.map(movie => (
              <li key={movie.movieId}>
                <button
                  onClick={() => handleSearchSelect(movie.movieId)}
                  style={{
                    width: '100%',
                    padding: '10px',
                    textAlign: 'left',
                    whiteSpace: 'nowrap',
                    overflow: 'hidden',
                    textOverflow: 'ellipsis',
                    display: 'block',
                    border: 'none',
                    background: 'none',
                    cursor: 'pointer'
                  }}
                  title={movie.title}
                >
                  {movie.title}
                </button>
              </li>
            ))}
          </ul>
        )}
      </div>

      {selectedMovie && (
        <div style={{ marginTop: '20px' }}>
          <h3>Title: {selectedMovie.title}</h3>
          <p><strong>Genre:</strong> {selectedMovie.genre.join(', ')}</p>
          <p>
            <strong>Avg Rating:</strong> {renderStars(selectedMovie.avgRating)}{" "}
            <span style={{ marginLeft: '8px' }}>
              ({selectedMovie.avgRating.toFixed(2)} / 5)
            </span>
          </p>
          <p>
            <strong>Votes:</strong>{" "}
            {Number.isFinite(selectedMovie.numRatings)
              ? selectedMovie.numRatings.toLocaleString()
              : "N/A"}
          </p>
        </div>
      )}
    </div>
  );
};

export default MovieResult;
