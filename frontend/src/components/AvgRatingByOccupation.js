// AvgRatingsByOccupation.js
import React, { useEffect, useState } from 'react';
import { PieChart, Pie, Cell, Tooltip, Legend, ResponsiveContainer } from 'recharts';

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

          // Skip empty lines
          if (!line) continue;

          const parts = line.split(',');

          // Only process lines with exactly 2 parts
          if (parts.length !== 2) continue;

          const rawCol1 = parts[0];
          const rawCol2 = parts[1];

          const occupation = rawCol1?.replace(/"/g, '').trim();
          const averageRating = parseFloat(rawCol2?.replace(/"/g, '').trim());

          const roundedRating = averageRating.toFixed(2);

          // Only push valid data
          if (occupation && !isNaN(roundedRating)) {
            result.push({ occupation, averageRating: parseFloat(roundedRating) });
          }
        }

        setData(result);
      });
  }, []);

  // Prepare data for the PieChart
  const pieData = data.map((item) => ({
    name: item.occupation,
    value: item.averageRating,
  }));

  // Define the color palette for the pie chart
  const COLORS = [
    '#ff6f61', '#6b5b95', '#88b04b', '#f7c948', '#ffb3e6', '#c9e2f2', '#f0b27a', '#d2b4de', '#85c1ae', '#f5b7b1',
  ];

  return (
    <div>
      <h2 className="text-xl font-bold mb-4">Average Ratings by Occupation</h2>

      {data.length === 0 ? (
        <p>Loading data...</p>
      ) : (
        <ResponsiveContainer width="100%" height={650}>
          <PieChart>
            <Pie
              data={pieData}
              dataKey="value"
              nameKey="name"
              cx="50%"
              cy="50%"
              outerRadius={150}
              label
            >
              {pieData.map((entry, index) => (
                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
              ))}
            </Pie>
            <Tooltip />
            <Legend />
          </PieChart>
        </ResponsiveContainer>
      )}
    </div>
  );
};

export default AvgRatingsByOccupation;
