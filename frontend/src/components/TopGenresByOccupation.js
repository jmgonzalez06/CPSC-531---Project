import React, { useEffect, useState } from 'react';
import {
  PieChart, Pie, Cell, Tooltip, Legend, ResponsiveContainer,
} from 'recharts';

const COLORS = ['#8884d8', '#82ca9d', '#ffc658', '#ff8042', '#a4de6c', '#d0ed57', '#8dd1e1'];

const TopGenresByOccupation = () => {
  const [data, setData] = useState([]);
  const [selectedGenre, setSelectedGenre] = useState('');

  useEffect(() => {
    fetch('../output/TopGenresByOccupation_clean.csv')
      .then((res) => res.text())
      .then((text) => {
        const lines = text.trim().split(/\r?\n/);
        const result = [];

        for (let i = 1; i < lines.length; i++) {
          const line = lines[i].trim();
          if (!line) continue;

          const parts = line.match(/"([^"]+)","([^"]+)","([^"]+)"/);
          if (parts && parts.length === 4) {
            const occupation = parts[1];
            const genre = parts[2];
            const rating = parseFloat(parts[3]);

            if (!isNaN(rating)) {
              result.push({ occupation, genre, rating });
            }
          }
        }

        console.log('Parsed Top Genres by Occupation:', result);
        setData(result);
      });
  }, []);

  const genres = [...new Set(data.map(item => item.genre))];
  const filteredData = data.filter(item => item.genre === selectedGenre);

  const pieData = filteredData.map(({ occupation, rating }) => ({
    name: occupation,
    value: rating,
  }));

  return (
    <div className="p-4 max-w-xl mx-auto">
      <h2 className="text-2xl font-bold mb-4">Top Genres by Occupation</h2>

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

      {pieData.length > 0 && (
        <div style={{ width: '100%', height: 600 }}>
          <ResponsiveContainer>
            <PieChart>
              <Pie
                data={pieData}
                dataKey="value"
                nameKey="name"
                outerRadius={100}
                label={({ name, value }) => `${name}: ${value.toFixed(2)}`}
              >
                {pieData.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip formatter={(value) => value.toFixed(2)} />
              <Legend />
            </PieChart>
          </ResponsiveContainer>
        </div>
      )}

      {selectedGenre && pieData.length === 0 && (
        <p className="text-gray-500 mt-4">No data available for the selected genre.</p>
      )}
    </div>
  );
};

export default TopGenresByOccupation;
