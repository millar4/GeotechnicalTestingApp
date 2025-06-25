import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './additem.css';

const AddItem = () => {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    test: '',classification: '', group: '', symbol: '', parameters: '', testMethod: '',
    alt1: '', alt2: '', alt3: '', sampleType: '', fieldSampleMass: '',
    specimenType: '', specimenMass: '', specimenNumbers: '', specimenD: '',
    specimenL: '', specimenW: '', specimenH: '', specimenMaxGrainSize: '',
    specimenMaxGrainFraction: '', schdedulingNotes: ''
  });

  const [databaseTarget, setDatabaseTarget] = useState('database');
  const [showConfirm, setShowConfirm] = useState(false);
  const [showPasswordPrompt, setShowPasswordPrompt] = useState(false);
  const [password, setPassword] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setShowConfirm(true);
  };

  const handleConfirm = () => {
    setShowConfirm(false);
    setShowPasswordPrompt(true);
  };

  const handleFinalSubmit = async () => {
    const token = localStorage.getItem('token');

    try {
      const authResponse = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          username: localStorage.getItem('username'),
          password
        })
      });

      if (!authResponse.ok) {
        throw new Error('Authentication failed: incorrect password');
      }

      const url = `http://localhost:8080/${databaseTarget}/add`;


      const addResponse = await fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(formData)
      });

      if (!addResponse.ok) {
        throw new Error(`Failed to add item: ${addResponse.status}`);
      }

      alert('Item successfully added!');
      navigate('/');
    } catch (error) {
      console.error(error);
      alert(error.message);
      setShowPasswordPrompt(false);
    }
  };

  const fields = [
    "test", "classification" , "group", "symbol", "parameters", "testMethod",
    "alt1", "alt2", "alt3", "sampleType", "fieldSampleMass", "specimenType",
    "specimenMass", "specimenNumbers", "specimenD", "specimenL",
    "specimenW", "specimenH", "specimenMaxGrainSize", "specimenMaxGrainFraction", "schedulingNotes"
  ];

  const formatLabel = (label) =>
    label.replace(/([A-Z])/g, ' $1').replace(/^./, str => str.toUpperCase());

  return (
    <div className="add-item-container">
      <h2>Add New Item</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-row">
          <label htmlFor="databaseTarget">Target Database:</label>
          <select
            id="databaseTarget"
            value={databaseTarget}
            onChange={(e) => setDatabaseTarget(e.target.value)}
          >
            <option value="database">Soil</option>
            <option value="aggregate">Aggregate</option>
            <option value="rocks">Rock</option>
            <option value="concrete">Concrete</option>
          </select>
        </div>

        {fields.map((field, idx) => (
          <div className="form-row" key={field}>
            <label htmlFor={field}>{formatLabel(field)}:</label>
            <input
              id={field}
              name={field}
              value={formData[field]}
              placeholder="None"
              onChange={handleChange}
              autoFocus={idx === 0}
              onKeyDown={(e) => {
                if (e.key === 'Enter') {
                  e.preventDefault();
                  const nextInput = document.querySelector(`#${fields[idx + 1]}`);
                  if (nextInput) nextInput.focus();
                }
              }}
            />
          </div>
        ))}

        <div className="button-group">
          <button type="submit">Submit</button>
          <button type="button" onClick={() => navigate(-1)}>Cancel</button>
        </div>
      </form>

      {showConfirm && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h3>Are you sure to add a new item?</h3>
            <table>
              <tbody>
                {fields.map(field => (
                  <tr key={field}>
                    <td><strong>{formatLabel(field)}:</strong></td>
                    <td>{formData[field]}</td>
                  </tr>
                ))}
              </tbody>
            </table>
            <div className="modal-buttons">
              <button onClick={handleConfirm}>Confirm</button>
              <button onClick={() => setShowConfirm(false)}>Cancel</button>
            </div>
          </div>
        </div>
      )}

      {showPasswordPrompt && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h3>Please enter your password to confirm</h3>
            <input
              type="password"
              placeholder="Enter password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <div className="modal-buttons">
              <button onClick={handleFinalSubmit}>Submit</button>
              <button onClick={() => setShowPasswordPrompt(false)}>Cancel</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default AddItem;
