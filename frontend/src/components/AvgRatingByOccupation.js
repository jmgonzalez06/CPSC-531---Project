import React, { useEffect, useState } from 'react';
import {
  BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, Cell
} from 'recharts';

const COLORS = [
  '#8884d8', '#82ca9d', '#ffc658', '#ff8042', '#a4de6c',
  '#d0ed57', '#8dd1e1', '#d888d8', '#84b6f4', '#f48fb1',
  '#ffcc80', '#80cbc4', '#bcaaa4', '#aed581', '#ce93d8',
];

const AvgRatingsByOccupation = () => {
  const [data, setData] = useState([]);

  useEffect(() => {
    fetch('../output/AvgRatingByOccupation_clean.csv')
      .then((response) => response.text())
      .then((csvText) => {
        const lines = csvText.trim().split('\n');
        const result = [];

        for (let i = 1; i < lines.length; i++) {
          const line = lines[i].trim();
          if (!line) continue;

          const parts = line.split(',');
          if (parts.length !== 2) continue;

          const occupation = parts[0]?.replace(/"/g, '').trim();
          const avgRating = parseFloat(parts[1]?.replace(/"/g, '').trim());

          if (occupation && !isNaN(avgRating)) {
            result.push({ occupation, averageRating: parseFloat(avgRating.toFixed(2)) });
          }
        }

        setData(result);
      });
  }, []);

  return (
    <div>
      <h2 className="text-xl font-bold mb-4">Average Ratings by Occupation</h2>

      {data.length === 0 ? (
        <p>Loading data...</p>
      ) : (
        <ResponsiveContainer width="100%" height={500}>
          <BarChart
            data={data}
            layout="vertical"
            margin={{ top: 20, right: 30, left: 100, bottom: 20 }}
          >
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis type="number" domain={[0, 5]} />
            <YAxis dataKey="occupation" type="category" width={150} />
            <Tooltip />
            <Legend />
            <Bar dataKey="averageRating">
              {data.map((entry, index) => (
                <Cell
                  key={`cell-${index}`}
                  fill={COLORS[index % COLORS.length]}
                />
              ))}
            </Bar>
          </BarChart>
        </ResponsiveContainer>
      )}
    </div>
  );
};

export default AvgRatingsByOccupation;
