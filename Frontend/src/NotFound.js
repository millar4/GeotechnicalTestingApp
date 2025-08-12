// src/pages/NotFound.js
import React from 'react';
import './NotFound.css';

const NotFound = () => {
  return (
    <div className="not-found-container">
      <h2>404 - Page Not Found</h2>
      <p>The page you are looking for might have been removed or is temporarily unavailable.</p>
      <p>If you believe this is an error, please contact support or go back to the homepage.</p>
      <button onClick={() => window.location.href = '/'}>Go to Homepage</button>
    </div>
  );
};

export default NotFound;
