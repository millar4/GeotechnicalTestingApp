import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './EditTest.css';

const EditTest = () => {
  const navigate = useNavigate();
  const { state } = useLocation();
  const initialData = (state && state.details) || {};

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
    specimenMaxGrainFraction: initialData.specimenMaxGrainFraction || ''
  });

  const testId = initialData.id;

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
      const authResponse = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          username: currentUsername,
          password
        })
      });

      if (!authResponse.ok) {
        throw new Error('Authentication failed: incorrect password');
      }

      if (actionType === 'update') {
        const updateResponse = await fetch(`http://localhost:8080/database/update/${testId}`, {
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
        if (initialData.username === currentUsername) {
          const confirmed = window.confirm("You are about to delete your own account. You will be logged out. Continue?");
          if (!confirmed) return;
        }

        const deleteResponse = await fetch(`http://localhost:8080/database/delete/${testId}`, {
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


  return (
    <div className="edit-item-container">
      <h2>Edit Test</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-row">
          <label htmlFor="test">Test:</label>
          <input id="test" name="test" value={formData.test} onChange={handleChange} autoFocus placeholder="Enter test name" />
        </div>
        <div className="form-row">
          <label htmlFor="group">Group:</label>
          <input id="group" name="group" value={formData.group} onChange={handleChange} placeholder="Enter group" />
        </div>
        <div className="form-row">
          <label htmlFor="classification">AGS Value:</label>
          <input id="classification" name="classification" value={formData.group} onChange={handleChange} placeholder="Enter AGS Value" />
        </div>
        <div className="form-row">
          <label htmlFor="symbol">Symbol:</label>
          <input id="symbol" name="symbol" value={formData.symbol} onChange={handleChange} placeholder="Enter symbol" />
        </div>
        <div className="form-row">
          <label htmlFor="parameters">Parameters:</label>
          <input id="parameters" name="parameters" value={formData.parameters} onChange={handleChange} placeholder="Enter parameters" />
        </div>
        <div className="form-row">
          <label htmlFor="testMethod">Test Method:</label>
          <input id="testMethod" name="testMethod" value={formData.testMethod} onChange={handleChange} placeholder="Enter test method" />
        </div>
        <div className="form-row">
          <label htmlFor="alt1">Alt1:</label>
          <input id="alt1" name="alt1" value={formData.alt1} onChange={handleChange} placeholder="Enter Alt1" />
        </div>
        <div className="form-row">
          <label htmlFor="alt2">Alt2:</label>
          <input id="alt2" name="alt2" value={formData.alt2} onChange={handleChange} placeholder="Enter Alt2" />
        </div>
        <div className="form-row">
          <label htmlFor="alt3">Alt3:</label>
          <input id="alt3" name="alt3" value={formData.alt3} onChange={handleChange} placeholder="Enter Alt3" />
        </div>
        <div className="form-row">
          <label htmlFor="sampleType">Sample Type:</label>
          <input id="sampleType" name="sampleType" value={formData.sampleType} onChange={handleChange} placeholder="Enter sample type" />
        </div>
        <div className="form-row">
          <label htmlFor="fieldSampleMass">Field Sample Mass:</label>
          <input id="fieldSampleMass" name="fieldSampleMass" value={formData.fieldSampleMass} onChange={handleChange} placeholder="Enter field sample mass" />
        </div>
        <div className="form-row">
          <label htmlFor="specimenType">Specimen Type:</label>
          <input id="specimenType" name="specimenType" value={formData.specimenType} onChange={handleChange} placeholder="Enter specimen type" />
        </div>
        <div className="form-row">
          <label htmlFor="specimenMass">Specimen Mass:</label>
          <input id="specimenMass" name="specimenMass" value={formData.specimenMass} onChange={handleChange} placeholder="Enter specimen mass" />
        </div>
        <div className="form-row">
          <label htmlFor="specimenNumbers">Specimen Numbers:</label>
          <input id="specimenNumbers" name="specimenNumbers" value={formData.specimenNumbers} onChange={handleChange} placeholder="Enter specimen numbers" />
        </div>
        <div className="form-row">
          <label htmlFor="specimenD">Specimen D:</label>
          <input id="specimenD" name="specimenD" value={formData.specimenD} onChange={handleChange} placeholder="Enter specimen D" />
        </div>
        <div className="form-row">
          <label htmlFor="specimenL">Specimen L:</label>
          <input id="specimenL" name="specimenL" value={formData.specimenL} onChange={handleChange} placeholder="Enter specimen L" />
        </div>
        <div className="form-row">
          <label htmlFor="specimenW">Specimen W:</label>
          <input id="specimenW" name="specimenW" value={formData.specimenW} onChange={handleChange} placeholder="Enter specimen W" />
        </div>
        <div className="form-row">
          <label htmlFor="specimenH">Specimen H:</label>
          <input id="specimenH" name="specimenH" value={formData.specimenH} onChange={handleChange} placeholder="Enter specimen H" />
        </div>
        <div className="form-row">
          <label htmlFor="specimenMaxGrainSize">Specimen Max Grain Size:</label>
          <input id="specimenMaxGrainSize" name="specimenMaxGrainSize" value={formData.specimenMaxGrainSize} onChange={handleChange} placeholder="Enter specimen max grain size" />
        </div>
        <div className="form-row">
          <label htmlFor="specimenMaxGrainFraction">Specimen Max Grain Fraction:</label>
          <input id="specimenMaxGrainFraction" name="specimenMaxGrainFraction" value={formData.specimenMaxGrainFraction} onChange={handleChange} placeholder="Enter specimen max grain fraction" />
        </div>
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
