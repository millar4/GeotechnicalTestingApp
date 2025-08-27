import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function Home() {
  const [searchContent, setSearchContent] = useState('');
  const [placeholder, setPlaceholder] = useState('Please select a search mode to search');
  const [pattern, setPattern] = useState('Quick Search');
  const [databaseType, setDatabaseType] = useState('all');
  const [searchHistory, setSearchHistory] = useState([]);
  const [showHistory, setShowHistory] = useState(false);
  const [allDatabaseData, setAllDatabaseData] = useState({});
  const [suggestions, setSuggestions] = useState([]);
  const navigate = useNavigate();

  const headers = {}; // Add auth headers if needed

  // === Fetch selected database data for predictive search ===
  useEffect(() => {
    const fetchDatabase = async () => {
      let dbTypes =
        databaseType === 'all'
          ? ['database', 'aggregate', 'rocks', 'concrete', 'inSituTest', 'earthworks']
          : [databaseType === 'soil' ? 'database' : databaseType];

      const dbData = {};
      for (let db of dbTypes) {
        try {
          const res = await fetch(`http://localhost:8080/${db}/all`, { headers });
          if (res.ok) {
            const data = await res.json();
            dbData[db] = data;
          }
        } catch (err) {
          console.warn(`Failed to fetch ${db}:`, err);
        }
      }
      setAllDatabaseData(dbData);
    };

    fetchDatabase();
  }, [databaseType]);

  // === Predictive search suggestions ===
useEffect(() => {
  if (!searchContent) {
    setSuggestions([]);
    return;
  }

  const lowerQuery = searchContent.toLowerCase();
  let filteredData = [];

  if (databaseType === 'all') {
    Object.values(allDatabaseData).forEach(arr => {
      filteredData.push(...arr);
    });
  } else {
    const dbKey = databaseType === 'soil' ? 'database' : databaseType;
    filteredData = allDatabaseData[dbKey] || [];
  }

  // Split into two arrays: startsWith and contains
  const startsWithMatches = [];
  const containsMatches = [];

  filteredData.forEach(item => {
    const searchable = [item.test, item.group, item.parameters, item.testMethod, item.classification]
      .filter(Boolean)
      .join(' ')
      .toLowerCase();

    if (searchable.startsWith(lowerQuery)) {
      startsWithMatches.push(item);
    } else if (searchable.includes(lowerQuery)) {
      containsMatches.push(item);
    }
  });

  const matches = [...startsWithMatches, ...containsMatches].slice(0, 5);
  setSuggestions(matches);
}, [searchContent, allDatabaseData, databaseType]);

  // === Search history functions ===
  const saveHistory = item => {
    let history = JSON.parse(localStorage.getItem('searchhistory')) || [];
    history = history.filter(h => h.content !== item.content);
    history.unshift(item);
    localStorage.setItem('searchhistory', JSON.stringify(history));
  };

  const deleteHistoryItem = item => {
    let history = JSON.parse(localStorage.getItem('searchhistory')) || [];
    history = history.filter(h => h.content !== item.content);
    localStorage.setItem('searchhistory', JSON.stringify(history));
    setSearchHistory(JSON.parse(localStorage.getItem('searchhistory')) || []);
  };

  const handleFocus = () => {
    const storedHistory = JSON.parse(localStorage.getItem('searchhistory')) || [];
    setSearchHistory(storedHistory);
    setShowHistory(true);
  };

  const handleClickHistory = item => {
    setSearchContent(item.content);
    setPattern(item.mode);
    setShowHistory(false);
  };

  const handleBlur = e => {
    if (!e.currentTarget.contains(e.relatedTarget)) {
      setShowHistory(false);
    }
  };

  // === Pattern & database change handlers ===
  const handlePatternChange = e => {
    const value = e.target.value;
    setPattern(value);
    switch (value) {
      case 'Quick Search':
        setPlaceholder('Quick Search');
        break;
      case 'Test name':
        setPlaceholder('Please enter test name to search');
        break;
      case 'Classification':
        setPlaceholder('Please enter AGS to search');
        break;
      case 'Test group':
        setPlaceholder('Please enter test group to search');
        break;
      case 'Test parameters':
        setPlaceholder('Please enter test parameters to search');
        break;
      case 'Test method':
        setPlaceholder('Please enter test method to search');
        break;
      default:
        setPlaceholder('Please select a search mode on the left');
    }
  };

  const handleDatabaseChange = e => setDatabaseType(e.target.value);

  // === Search function ===
  const performSearch = e => {
    e.preventDefault();
    if (!searchContent.trim()) return;

    saveHistory({ content: searchContent, mode: pattern });

    navigate('/AllTestListPage', {
      state: { pattern, searchcontent: searchContent, databaseType }
    });
  };

  const pressEnter = e => {
    if (e.key === 'Enter') performSearch(e);
  };

  return (
    <div>
      <header className="App-header">
        <div className="floating-island">
          <p>TestFinder</p>
          <small>Use this search engine to look up parameters, test methods and AGS values</small>
          <div className="search-container">
            <div className="selectorbox">
              <select className="selector" value={pattern} onChange={handlePatternChange}>
                <option value="Quick Search">Quick Search</option>
                <option value="Classification">AGS Value</option>
                <option value="Test group">Test group</option>
                <option value="Test parameters">Test parameters</option>
                <option value="Test method">Test method</option>
              </select>
              <select className="selector" value={databaseType} onChange={handleDatabaseChange}>
                <option value="all">All</option>
                <option value="soil">Soil</option>
                <option value="rocks">Rock</option>
                <option value="earthworks">Earthworks</option>
                <option value="inSituTest">In Situ</option>
                <option value="aggregate">Aggregate</option>
                <option value="concrete">Concrete</option>
              </select>
            </div>

            <div className="input-wrapper">
              <span className="search-icon">🔍</span>
              <input
                type="text"
                className="search-box"
                placeholder={placeholder}
                value={searchContent}
                onChange={e => setSearchContent(e.target.value)}
                onKeyDown={pressEnter}
                onFocus={handleFocus}
                onBlur={handleBlur}
              />
              <button className="search-button" onClick={performSearch}>
                Search
              </button>

              {/* === Predictive search suggestions & history === */}
              {(showHistory || suggestions.length > 0) && (
                <div className="history-dropdown" onMouseDown={e => e.preventDefault()}>
                  {searchContent
                    ? suggestions.map((item, idx) => (
                        <div
                          key={idx}
                          className="history-item"
                          onClick={() => {
                            setSearchContent(item.test);
                            navigate('/AllTestListPage', {
                              state: { pattern: 'Quick Search', searchcontent: item.test, databaseType }
                            });
                          }}
                        >
                          {item.test} <span className="history-mode">({item.group})</span>
                        </div>
                      ))
                    : searchHistory.map((item, idx) => (
                        <div key={idx} className="history-item" onClick={() => handleClickHistory(item)}>
                          {item.content} <span className="history-mode">({item.mode})</span>
                          <span
                            className="delete-icon"
                            onClick={e => {
                              e.stopPropagation();
                              deleteHistoryItem(item);
                            }}
                          >
                            ×
                          </span>
                        </div>
                      ))}
                </div>
              )}
            </div>
          </div>
        </div>
      </header>
    </div>
  );
}

export default Home;
