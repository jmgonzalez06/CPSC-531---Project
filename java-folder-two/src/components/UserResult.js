import React, { useEffect, useState } from 'react';
import './UserResult.css'; // Optional styling

//We're going to wait for the user_restults page to make this work

const UserResult = () => {
  const [users, setUsers] = useState([]);

  useEffect(() => {
    fetch('../output/user_results.csv')
      .then(res => res.text())
      .then(text => {
        const lines = text.trim().split('\n');
        const headers = ['userId', 'age', 'gender', 'occupation', 'zipCode'];

        const userData = lines.map(line => {
          const values = line.split('|');
          const user = {};
          headers.forEach((header, i) => {
            user[header] = values[i];
          });
          return user;
        });

        setUsers(userData);
      })
      .catch(err => console.error('Error loading user data:', err));
  }, []);

  return (
    <div className="user-result">
      <h2>User Results</h2>
      {users.length === 0 ? (
        <p>Loading users...</p>
      ) : (
        <table className="user-table">
          <thead>
            <tr>
              <th>User ID</th>
              <th>Age</th>
              <th>Gender</th>
              <th>Occupation</th>
              <th>Zip Code</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user, idx) => (
              <tr key={idx}>
                <td>{user.userId}</td>
                <td>{user.age}</td>
                <td>{user.gender}</td>
                <td>{user.occupation}</td>
                <td>{user.zipCode}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default UserResult;
