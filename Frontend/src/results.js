//not finished
import React from 'react';
import { useLocation } from 'react-router-dom';


function Result() {
    const location = useLocation();
    const { pattern, searchcontent } =  location.state || { pattern: '', searchcontent: '' } 
    return (
      <div>
        <h1>Searching result</h1>
        <p>Displaying tests that best match "{searchcontent}" with pattern "{pattern}"</p>
        
      </div>
    );
  }
  



export default Result;