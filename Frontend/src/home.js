import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function Home() {
  const [searchcontent, updatecontent] = useState('');
  const [placeholder, updatePlaceholder] = useState('Please select a search mode to search');
  const [pattern, updatePattern] = useState('Quick Search');
  const [databaseType, setDatabaseType] = useState('soil'); // NEW: Database type selection
  const [searchhistory, updatesearchhistory] = useState([]);
  const [showhistory, updateshowhistory] = useState(false);
  const navigate = useNavigate();

  const getTransformedHistory = () => {
    const storedHistory = JSON.parse(localStorage.getItem('searchhistory')) || [];
    const thehistory = storedHistory
      .map((item) => {
        if (typeof item === 'string') {
          return { content: item, mode: 'Unknown' };
        } else if (item && typeof item.content === 'string') {
          return item;
        } else {
          return { content: '', mode: 'Unknown' };
        }
      })

      .filter((item) => item.content.trim() !== '');
    return thehistory;
  };

  const searchfunction = (p) => {
    p.preventDefault();
    savethehistory({ content: searchcontent, mode: pattern });
    // NEW: Pass databaseType in state
    navigate('/AllTestListPage', { state: { pattern, searchcontent, databaseType } });
  };

  const pressEnter = (p) => {
    if (p.key === 'Enter') {
      searchfunction(p);
    }
  };

  const savethehistory = (searchItem) => {
  let thehistory = JSON.parse(localStorage.getItem('searchhistory')) || [];
  thehistory = thehistory.filter(item => item.content !== searchItem.content);
  thehistory.unshift(searchItem);
  localStorage.setItem('searchhistory', JSON.stringify(thehistory));
  };

  const deletehistoryitem = (deleteditem) => {
    let thehistory = JSON.parse(localStorage.getItem('searchhistory')) || [];
    thehistory = thehistory.filter(item => {
      if (typeof item === 'string') {
        return item !== deleteditem.content;
      } else if (item && typeof item.content === 'string') {
        return item.content !== deleteditem.content;
      }
      return false;
    });
    localStorage.setItem('searchhistory', JSON.stringify(thehistory));
    const newHistory = getTransformedHistory();
    updatesearchhistory(newHistory);
    updateshowhistory(true);
  };



  const handleFocus = () => {
    const storedHistory = JSON.parse(localStorage.getItem('searchhistory')) || [];  
    const thehistory = storedHistory.map((item, idx) => {
      if (typeof item === 'string') {
        return { content: item, mode: 'Unknown' };
      } else if (item && typeof item.content === 'string') {
        return item;
      } else {
        return { content: '', mode: 'Unknown' };
      }
    })
    .filter((item, idx) => item.content); 
    updatesearchhistory(thehistory);
    updateshowhistory(true);
  };


  const handleClickHistory = (item) => {
    updatecontent(item.content);
    updatePattern(item.mode); 
    handlePatternChange({ target: { value: item.mode } }); 
    updateshowhistory(false);
  };
  

  const handleBlur = (e) => {
    if (!e.currentTarget.contains(e.relatedTarget)) {
      updateshowhistory(false);
    }
  };

  const handlePatternChange = (e) => {
    const selectedPattern = e.target.value;
    updatePattern(selectedPattern);
    switch (selectedPattern) {
      case 'Quick Search':
        updatePlaceholder('Quick Search');
        break;
      case 'Test name':
        updatePlaceholder('Please enter test name to search');
        break;
      case 'Classification':
        updatePlaceholder('Please enter AGS to search');
        break;
      case 'Test group':
        updatePlaceholder('Please enter test group to search');
        break;
      case 'Test parameters':
        updatePlaceholder('Please enter test parameters to search');
        break;
      case 'Test method':
        updatePlaceholder('Please enter test method to search');
        break;
      default:
        updatePlaceholder('Please select a search mode on the left');
    }
  };

  // NEW: Handle database type change
  const handleDatabaseChange = (e) => {
    setDatabaseType(e.target.value);
  };

  return (
    <div>
      <header className="App-header">
        <div className="floating-island">
          <p>GeoTest Finder</p>
          <div className="search-container">
            <div className="selectorbox">
              <select className="selector" value={pattern} onChange={handlePatternChange}>
                <option value="Quick Search">Quick Search</option>
                <option value="Classification">AGS Value</option>
                <option value="Test group">Test group</option>
                <option value="Test parameters">Test parameters</option>
                <option value="Test method">Test method</option>
              </select>
              {/* NEW: Database type selection */}
              <select className="selector" value={databaseType} onChange={handleDatabaseChange}>
                <option value="soil">Soil</option>
                <option value="aggregate">Aggregate</option>
                <option value="rocks">Rock</option>
                <option value="concrete">Concrete</option>
                <option value="inSituTest">In Situ</option>
                <option value="earthworks">Earthworks</option>
              </select>
            </div>
            <div className="input-wrapper">
              <span className="search-icon">🔍</span>
              <input
                type="text"
                className="search-box"
                placeholder={placeholder}
                value={searchcontent}
                onChange={(e) => updatecontent(e.target.value)}
                onKeyDown={pressEnter}
                onFocus={handleFocus}
                onBlur={handleBlur}
              />
              <button className="search-button" onClick={searchfunction}>
                Search
              </button>
              {showhistory && searchhistory.length > 0 && (
                <div className="history-dropdown" onMouseDown={(e) => e.preventDefault()}>
                  {[...searchhistory]
                    // .filter(item => typeof item === 'string')
                    .sort((a, b) => {
                      const startsWithA = a.content.startsWith(searchcontent);
                      const startsWithB = b.content.startsWith(searchcontent);
                      if (startsWithA && !startsWithB) return -1;
                      if (!startsWithA && startsWithB) return 1;
                      return 0;
                    })
                    .map((item, index) => (
                      <div key={index} className="history-item" onClick={() => handleClickHistory(item)}>
                        {item.content} <span className="history-mode">({item.mode})</span>
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
            </div>
          </div>
        </div>
      </header>
    </div>
  );
}

export default Home;
