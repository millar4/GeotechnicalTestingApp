import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter as Router } from 'react-router-dom';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { SelectedTestsProvider } from './SelectedTestsContext';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <SelectedTestsProvider>
      <Router>
        <App />
      </Router>
    </SelectedTestsProvider>
  </React.StrictMode>
);

// Only keep this — remove the duplicate ReactDOM.render block
reportWebVitals();
