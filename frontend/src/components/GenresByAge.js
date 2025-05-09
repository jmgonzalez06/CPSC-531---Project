import React, { useEffect, useState } from 'react';
import {
  BarChart, Bar, XAxis, YAxis, Tooltip, Legend, ResponsiveContainer, Cell
} from 'recharts';

const COLORS = ['#8884d8', '#82ca9d', '#ffc658', '#ff8042', '#a4de6c'];

const GenresByAge = () => {
  const [data, setData] = useState([]);
  const [selectedGenre, setSelectedGenre] = useState('');

  useEffect(() => {
    fetch('../output/GenresByAge_clean.csv')
      .then((res) => res.text())
      .then((text) => {
        const lines = text.trim().split(/\r?\n/);
        const result = [];

        for (let i = 1; i < lines.length; i++) {
          const line = lines[i].trim();
          if (!line) continue;

          const parts = line.match(/"([^"]+)","([^"]+)","([^"]+)"/);
          if (parts && parts.length === 4) {
            const ageGroup = parts[1];
            const genre = parts[2];
            const rating = parseFloat(parts[3]);

            if (!isNaN(rating)) {
              result.push({ ageGroup, genre, rating });
            }
          }
        }

        setData(result);
      });
  }, []);

  const genres = [...new Set(data.map(item => item.genre))];
  const filteredData = data.filter(item => item.genre === selectedGenre);

  const barData = filteredData.map(({ ageGroup, rating }) => ({
    name: ageGroup,
    Rating: rating,
  }));

  return (
    <div className="p-4 max-w-xl mx-auto">
      <h2 className="text-2xl font-bold mb-4">Average Genre Ratings by Age Group</h2>

      <select
        className="border rounded p-2 mb-4"
        onChange={(e) => setSelectedGenre(e.target.value)}
        value={selectedGenre}
      >
        <option value="">Select Genre</option>
        {genres.map((g, i) => (
          <option key={i} value={g}>{g}</option>
        ))}
      </select>

      {barData.length > 0 && (
        <div style={{ width: '100%', height: 500 }}>
          <ResponsiveContainer>
            <BarChart
              data={barData}
              layout="vertical"
              margin={{ top: 0, right: 150, left: 200, bottom: 0 }}
            >
              <XAxis type="number" />
              <YAxis dataKey="name" type="category" />
              <Tooltip formatter={(value) => value.toFixed(2)} />
              <Legend />
              <Bar
                dataKey="Rating"
                fill="#8884d8"
                label={{ position: 'right', formatter: (value) => value.toFixed(2) }}
              >
                {barData.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
              </Bar>
            </BarChart>
          </ResponsiveContainer>
        </div>
      )}

      {selectedGenre && barData.length === 0 && (
        <p className="text-gray-500 mt-4">No data available for the selected genre.</p>
      )}
    </div>
  );
};

export default GenresByAge;
