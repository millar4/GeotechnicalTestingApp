import { useNavigate } from 'react-router-dom';
import React, { useState } from 'react';
import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Theupperbar from './theupperbar'
import Home from './home'
import About from './about';
import PaginatedBoxes from './AllTestListPage'
import Result from './results'
import AddItem from './additem'
import EditTest from './EditTest';
import AccountManagement from './AccountManagement';

function App() {
  const [searchcontent, updatecontent] = useState('');
  const navigate = useNavigate();
  const [pattern, updatepattern] = useState('Quick Search');
  const [searchhistory, updatesearchhistory] = useState([]);
  const [showhistory, updateshowhistory] = useState(false);
  const [databaseType, setDatabaseType] = useState('soil'); 
  

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
    console.log("searchfunction triggered", searchcontent, pattern, databaseType);
    savethehistory({ content: searchcontent, mode: pattern });
    navigate('/AllTestListPage', { state: { pattern, searchcontent, databaseType } });
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
    // const thepattern = storedHistory
    .filter((item, idx) => item.content); 
    updatesearchhistory(thehistory);
    updateshowhistory(true);
    // updatepattern(item.pattern)
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
         <Route path="/edititem" element={<EditTest />} />
         <Route path="/accountmanagement" element={<AccountManagement />} />
         </Routes>
         </div>
    </div>
  );
}


export default App;
