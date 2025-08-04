import { useNavigate } from 'react-router-dom';
import React, { useState, useEffect } from 'react';
import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Theupperbar from './theupperbar';
import Home from './home';
import About from './about';
import PaginatedBoxes from './AllTestListPage';
import Result from './results';
import AddItem from './additem';
import EditTest from './EditTest';
import AccountManagement from './AccountManagement';
import PrintableData from './PrintableData';  // Import PrintableData
import Footer from './footer'
import NotFound from './NotFound'; 

function App() {
  const [searchcontent, updatecontent] = useState('');
  const [pattern, updatepattern] = useState('Quick Search');
  const [searchhistory, updatesearchhistory] = useState([]);
  const [showhistory, updateshowhistory] = useState(false);
  const [databaseType, setDatabaseType] = useState('soil');
  const [jumpPage, setJumpPage] = useState(1);
  const navigate = useNavigate();

  // Initialize search history from local storage
  useEffect(() => {
    const storedHistory = JSON.parse(localStorage.getItem('searchhistory')) || [];
    const formattedHistory = storedHistory.map((item) => {
      if (typeof item === 'string') {
        return { content: item, mode: 'Unknown' };
      } else if (item && typeof item.content === 'string') {
        return item;
      } else {
        return { content: '', mode: 'Unknown' };
      }
    }).filter((item) => item.content.trim() !== '');
    updatesearchhistory(formattedHistory);
  }, []);

  const searchfunction = (p) => {
    p.preventDefault();
    console.log("searchfunction triggered", searchcontent, pattern, databaseType);
    savethehistory({ content: searchcontent, mode: pattern });
    navigate('/AllTestListPage', { state: { pattern, searchcontent, databaseType } });
  };

  const savethehistory = (searchItem) => {
    let thehistory = JSON.parse(localStorage.getItem('searchhistory')) || [];
    thehistory = thehistory.filter(item => item.content !== searchItem.content);
    thehistory.unshift(searchItem);
    localStorage.setItem('searchhistory', JSON.stringify(thehistory));
    updatesearchhistory([searchItem, ...thehistory]);
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
    updatesearchhistory(thehistory);
    updateshowhistory(true);
  };

  const handleFocus = () => {
    updateshowhistory(true);
  };

  const handleClickHistory = (item) => {
    updatecontent(item.content);
    updateshowhistory(false);
  };

  const handleBlur = (e) => {
    if (!e.currentTarget.contains(e.relatedTarget)) {
      updateshowhistory(false);
    }
  };

  // Details to be passed to PrintableData
  const details = {
    test: 'Test Name',
    symbol: 'TST',
    group: 'Group A',
    classification: 'AGS1',
    param: 'Param A',
  };

  // Format data to be displayed
  const formatData = (data, searchcontent, pattern, field) => {
    // Custom formatting logic if needed
    return data;
  };

  return (
    <div className="App">
      <Theupperbar
        searchcontent={searchcontent}
        updatecontent={updatecontent}
        searchfunction={searchfunction}
        searchhistory={searchhistory}
        showhistory={showhistory}
        handleFocus={handleFocus}
        handleBlur={handleBlur}
        deletehistoryitem={deletehistoryitem}
        handleClickHistory={handleClickHistory}
        pattern={pattern}
        updatepattern={updatepattern}
        databaseType={databaseType}
        setDatabaseType={setDatabaseType}
      />
      <div style={{ paddingTop: '60px' }}>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/About" element={<About />} />
          <Route path="/AllTestListPage" element={<PaginatedBoxes />} />
          <Route path="/result" element={<Result />} />
          <Route path="/additem" element={<AddItem />} />
          <Route path="/accountmanagement" element={<AccountManagement />} />
          <Route path="/edititem/:targetDatabase" element={<EditTest />} />
          <Route path="*" element={<NotFound />} />

    
          {/* Render PrintableData on a new route */}
          <Route path="/printable" element={
            <PrintableData
              details={details}
              searchcontent={searchcontent}
              pattern={pattern}
              formatData={formatData}
            />
          } />
        </Routes>
      </div>
      <Footer />
    </div>
  );
}

export default App;
