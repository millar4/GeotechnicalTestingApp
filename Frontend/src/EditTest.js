import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './EditTest.css';

const EditTest = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const state = location.state;
  console.log(location.state);
  // Determine entityType from state or fallback to path
  const getEntityType = () => {
    const knownTypes = ['aggregate', 'rock', 'concrete', 'inSituTest','database','earthworks'];
    const fromState = state?.type;
    if (fromState) return fromState;

    const path = location.pathname.toLowerCase();
    const matchedType = knownTypes.find(type => path.includes(type));
    return matchedType; 
  };

  const entityType = getEntityType();
  const initialData = state?.details || {};

  const [formData, setFormData] = useState({
    test: initialData.test || '',
    group: initialData.group || '',
    symbol: initialData.symbol || '',
    parameters: initialData.parameters || '',
    testMethod: initialData.testMethod || '',
    alt1: initialData.alt1 || '',
    alt2: initialData.alt2 || '',
    alt3: initialData.alt3 || '',
    sampleType: initialData.sampleType || '',
    fieldSampleMass: initialData.fieldSampleMass || '',
    specimenType: initialData.specimenType || '',
    specimenMass: initialData.specimenMass || '',
    specimenNumbers: initialData.specimenNumbers || '',
    specimenD: initialData.specimenD || '',
    specimenL: initialData.specimenL || '',
    specimenW: initialData.specimenW || '',
    specimenH: initialData.specimenH || '',
    specimenMaxGrainSize: initialData.specimenMaxGrainSize || '',
    specimenMaxGrainFraction: initialData.specimenMaxGrainFraction || '',
    databaseBelongsTo: initialData.databaseBelongsTo || ''
  });

  const testId = initialData.id;
  const targetDatabase = formData.databaseBelongsTo;

  const [showPasswordPrompt, setShowPasswordPrompt] = useState(false);
  const [password, setPassword] = useState('');
  const [actionType, setActionType] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setActionType('update');
    setShowPasswordPrompt(true);
  };

  const handleDelete = () => {
    setActionType('delete');
    setShowPasswordPrompt(true);
  };

  const handleFinalSubmit = async () => {
    const token = localStorage.getItem('token');
    const currentUsername = localStorage.getItem('username');

    try {
      // Authenticate user before proceeding
      const authResponse = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: currentUsername, password })
      });

      if (!authResponse.ok) throw new Error('Authentication failed: incorrect password');

      if (actionType === 'update') {
        // Handle Update Request
        const updateResponse = await fetch(`http://localhost:8080/${targetDatabase}/update/${testId}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body: JSON.stringify(formData)
        });

        if (!updateResponse.ok) {
          throw new Error(`Failed to update item: ${updateResponse.status}`);
        }

        alert('Test updated successfully!');
      } else if (actionType === 'delete') {
        // Handle Delete Request
        if (initialData.username === currentUsername) {
          const confirmed = window.confirm("You are about to delete your own account. You will be logged out. Continue?");
          if (!confirmed) return;
        }

        const deleteResponse = await fetch(`http://localhost:8080/${targetDatabase}/delete/${testId}`, {
          method: 'DELETE',
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        if (!deleteResponse.ok) {
          throw new Error(`Failed to delete item: ${deleteResponse.status}`);
        }

        alert('Test deleted successfully!');

        if (initialData.username === currentUsername) {
          localStorage.clear();
          alert('Your account was deleted. You have been logged out.');
          window.location.href = '/';
          return;
        }
      }

      navigate(-1);
    } catch (error) {
      console.error(error);
      alert(error.message);
    } finally {
      setShowPasswordPrompt(false);
    }
  };

  // Define dynamic form fields for each entity type
  const dynamicFields = {
    aggregate: ['test', 'group', 'symbol', 'parameters', 'testMethod', 'alt1', 'alt2', 'alt3', 'sampleType', 'fieldSampleMass', 'specimenType', 'specimenMass', 'specimenNumbers', 'specimenD', 'specimenL', 'specimenW', 'specimenH', 'specimenMaxGrainSize', 'specimenMaxGrainFraction'],
    rock: ['test', 'group', 'symbol', 'parameters', 'sampleType', 'fieldSampleMass', 'specimenType', 'specimenMass'],
    concrete: ['test', 'group', 'symbol', 'parameters', 'specimenType', 'specimenMass', 'specimenNumbers'],
    database: ['test', 'group', 'symbol', 'parameters', 'testMethod', 'specimenType', 'specimenMass', 'specimenD', 'specimenL', 'specimenW', 'specimenH'],
    inSituTest: ['Group','Test','Abbreviation or symbol','Test', 'parameters',' Default test method' ,'Alternative 1','Alternative 2','Alternative 3','Materials','Applications','databaseBelongsTo'],
    earthworks: ['Group','Test','Abbreviation or symbol','Test', 'parameters',' Default test method' ,'Alternative 1','Alternative 2','Alternative 3','Materials','Applications','databaseBelongsTo']
  };

  const selectedFields = dynamicFields[entityType] || dynamicFields.aggregate;

  return (
    <div className="edit-item-container">
      <h2>Edit Test</h2>
      <form onSubmit={handleSubmit}>
        {selectedFields.map(field => (
          <div className="form-row" key={field}>
            <label htmlFor={field}>{field.replace(/([A-Z])/g, ' $1')}:</label>
            <input
              id={field}
              name={field}
              value={formData[field] || ''}
              onChange={handleChange}
              placeholder={`Enter ${field.replace(/([A-Z])/g, ' $1')}`}
            />
          </div>
        ))}

        <div className="button-group">
          <button type="submit">Update</button>
          <button type="button" onClick={handleDelete}>Delete</button>
          <button type="button" onClick={() => navigate(-1)}>Cancel</button>
        </div>
      </form>

      {showPasswordPrompt && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h3>
              Please enter your password to {actionType === 'update' ? 'confirm update' : 'confirm deletion'}
            </h3>
            <input
              type="password"
              placeholder="Enter password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <div className="modal-buttons">
              <button onClick={handleFinalSubmit}>Confirm</button>
              <button onClick={() => setShowPasswordPrompt(false)}>Cancel</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default EditTest;
