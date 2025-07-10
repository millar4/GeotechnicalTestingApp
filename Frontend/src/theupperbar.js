import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './App.css'; 

function Theupperbar({ 
  searchcontent, 
  updatecontent, 
  searchfunction,
  searchhistory, 
  showhistory, 
  handleFocus,
  handleBlur, 
  deletehistoryitem, 
  handleClickHistory,
  pattern,
  updatepattern,
  databaseType,
  setDatabaseType
}) {
  const navigate = useNavigate();
  
  const [hovered, setHovered] = useState('');
  const [showLogin, setShowLogin] = useState(false);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState(null);
  
  // Use JWT token to determine login status
  const [token, setToken] = useState(localStorage.getItem('token') || null);
  const [userRole, setUserRole] = useState(localStorage.getItem('role') || null);

  // Keep localStorage in sync with token and role states
  useEffect(() => {
    if (token) {
      localStorage.setItem('token', token);
    } else {
      localStorage.removeItem('token');
    }
    if (userRole) {
      localStorage.setItem('role', userRole);
    } else {
      localStorage.removeItem('role');
    }
  }, [token, userRole]);

  // Periodically check backend version
  useEffect(() => {
    const interval = setInterval(() => {
      console.log("Checking backend version...");
      fetch("http://localhost:8080/api/version")
        .then(response => {
          console.log("Response status:", response.status);
          if (!response.ok) {
            throw new Error("Backend is down");
          }
          return response.json();
        })
        .then(data => {
          console.log("Version data:", data);
          const backendVersion = data.version;
          const storedVersion = localStorage.getItem("backendVersion");
          if (storedVersion && storedVersion !== String(backendVersion)) {
            console.log("Version changed, reloading...");
            localStorage.clear();
            localStorage.setItem("backendVersion", String(backendVersion));
            window.location.reload();
          } else if (!storedVersion) {
            localStorage.setItem("backendVersion", String(backendVersion));
          }
        })
        .catch(err => {
          console.error("Backend shutdown or version check failed:", err);
          localStorage.clear();
          window.location.reload();
        });
    }, 5000);
  
    return () => clearInterval(interval);
  }, []);

  // Handle login
  const handleLogin = async () => {
    setError(null);
    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password }),
        credentials: 'include'
      });

      if (response.status === 401 || response.status === 403) {
        alert("Backend unavailable or unauthorized. You have been logged out.");
        localStorage.clear();
        window.location.reload();
        return;
      }

      const data = await response.json();
      console.log('Login response data:', data);

      if (!response.ok) {
        throw new Error(data.message || 'Login failed');
      }
      setToken(data.token);
      setUserRole(data.role);

      localStorage.setItem('username', username);
      localStorage.setItem('isLoggedIn', 'true');

      setShowLogin(false);
      setUsername('');
      setPassword('');
      setError(null);
    } catch (err) {
      console.error('Login error:', err);
      setError(err.message);
    }
  };

  // Handle logout
  const handleLogout = () => {
    const confirmLogout = window.confirm('Are you sure you want to log out?');
    if (confirmLogout) {
      setToken(null);
      setUserRole(null);
      setUsername('');
      setPassword('');
      localStorage.removeItem('token');
      localStorage.removeItem('role');
      localStorage.removeItem('username');
      localStorage.removeItem('isLoggedIn');
    }
  };

  // Manage accounts
  const handleManageAccounts = () => {
    const role = localStorage.getItem('role');
    if (role !== 'ADMIN') {
      alert('You need ADMIN privileges to access this page.');
      return;
    }
    navigate('/accountmanagement');
  };

  const isLoggedIn = !!token;

  // Inline style variables
  const addline = {
    color: 'white',
    borderRight: '1px solid white',
    borderLeft: '1px solid white',
    padding: '0 4%',
    textDecoration: 'none',
    display: 'inline-block',
    height: '10vh', // Height of the links
    lineHeight: '10vh', // Vertical alignment within the height of the links
    boxSizing: 'border-box',
  };

  const buttonhover = {
    color: 'black',
    backgroundColor: 'white'
  };

  return (
    <nav
      style={{
        position: 'fixed',
        backgroundColor: '#8d1d1c',
        top: 0,
        left: 0,
        width: '100%',
        height: '100px', // Increased navbar height
        zIndex: 20,
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'flex-start', // Keeps the links at the top
        padding: '10px 20px', // Adjust padding to give some space inside
      }}
    >
      <button
        className="login-button"
        onClick={isLoggedIn ? handleLogout : () => setShowLogin(true)}
        style={{
          position: 'fixed',
          top: '55px',
          left: '187px',
          zIndex: 1000,
          backgroundColor: isLoggedIn ? 'red' : 'white',
          color: isLoggedIn ? 'white' : 'black'
        }}
      >
        {isLoggedIn ? 'Logout' : 'Login'}
      </button>

      <div className="logo-container">
        <img src="/LogoNB.png" alt="Company Logo" className="company-logo" />
      </div>

      <div className="left-buttons" style={{ marginLeft: '120px' }}>
        {isLoggedIn && (
          <button onClick={handleManageAccounts} className="account-button">
            Account Management
          </button>
        )}
      </div>

      
      <div className="centered-nav">
      {['Home', 'Search', 'Test List', 'About'].map((label) => {
        const routeMap = {
          'Home': 'https://soils.co.uk/',
          'Search': '/',
          'Test List': '/AllTestListPage',
          'About': '/about',
        };

        const isHovered = hovered === label;

        return (
          <Link
            key={label}
            to={routeMap[label]}
            onMouseEnter={() => setHovered(label)}
            onMouseLeave={() => setHovered('')}
            className="nav-link"
          >
            {label}
            <span className={`nav-underline ${isHovered ? 'active' : ''}`} />
          </Link>
        );
      })}
  </div>

      {/* Search form */}
      <form
        onSubmit={(e) => {
          e.preventDefault();
          searchfunction(e);
        }}
        className="search-bar-form"
      >
        <input
          type="text"
          placeholder="Quick Search"
          value={searchcontent}
          onChange={(e) => updatecontent(e.target.value)}
          onFocus={handleFocus}
          onBlur={handleBlur}
          className="search-input"
        />
        
        <button className="quick-search-button" type="submit">
          Search
        </button>

        {showhistory && searchhistory.length > 0 && (
          <div
            className="history-dropdown"
            onMouseDown={(e) => e.preventDefault()}
          >
            {[...searchhistory]
              .sort((a, b) => {
                const startsWithA = a.content.startsWith(searchcontent);
                const startsWithB = b.content.startsWith(searchcontent);
                if (startsWithA && !startsWithB) return -1;
                if (!startsWithA && startsWithB) return 1;
                return 0;
              })
              .map((item, index) => (
                <div
                  key={index}
                  className="history-item"
                  onClick={() => handleClickHistory(item)}
                >
                  {item.content}{' '}
                  <span className="history-mode">({item.mode})</span>
                  <span
                    className="delete-icon"
                    onClick={(e) => {
                      e.stopPropagation();
                      deletehistoryitem(item);
                    }}
                  >
                    ×
                  </span>
                </div>
              ))}
          </div>
        )}
      </form>

      {showLogin && (
        <div className="login-modal">
          <div className="login-modal-content">
            <h2>Login</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            <input
              type="text"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <button onClick={handleLogin}>Submit</button>
            <button onClick={() => setShowLogin(false)}>Cancel</button>
          </div>
        </div>
      )}
    </nav>
  );
}

export default Theupperbar;

