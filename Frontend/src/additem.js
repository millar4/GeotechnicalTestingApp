import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './additem.css';

const dynamicFields = {
  aggregate: [
    'test', 'group', 'classification', 'symbol', 'parameters', 'testMethod',
    'sampleType', 'fieldSampleMass', 'specimenType', 'specimenMass',
    'specimenNumbers', 'specimenMaxGrainSize', 'specimenMaxGrainFraction',
    'schedulingNotes', 'testDescription'
  ],
  rocks: [
    'test', 'group', 'classification', 'symbol', 'parameters', 'testMethod', 'alt1', 'alt2', 'alt3',
    'sampleType', 'fieldSampleMass', 'specimenType', 'specimenMass', 'specimenNumbers',
    'specimenD', 'specimenL', 'specimenW', 'specimenH', 'specimenMaxGrainSize',
    'specimenMaxGrainFraction', 'schedulingNotes', 'testDescription'
  ],
  concrete: [
    'test', 'group', 'classification', 'symbol', 'parameters', 'testMethod', 'alt1', 'alt2', 'alt3',
    'sampleType', 'fieldSampleMass', 'specimenType', 'specimenMass', 'specimenNumbers',
    'specimenD', 'specimenL', 'specimenW', 'specimenH', 'specimenMaxGrainSize',
    'specimenMaxGrainFraction', 'schedulingNotes', 'testDescription'
  ],
  database: [
    'test', 'group', 'classification', 'symbol', 'parameters', 'testMethod', 'alt1', 'alt2', 'alt3',
    'sampleType', 'fieldSampleMass', 'specimenType', 'specimenMass', 'specimenNumbers',
    'specimenD', 'specimenL', 'specimenW', 'specimenH', 'specimenMaxGrainSize',
    'specimenMaxGrainFraction', 'testDescription'
  ],
  inSituTest: [
    'test', 'group', 'classification', 'symbol', 'parameters', 'testMethod', 'alt1', 'alt2', 'alt3',
    'sampleType', 'materials', 'applications', 'testDescription'
  ],
  earthworks: [
    'test', 'group', 'classification', 'symbol', 'parameters', 'testMethod',
    'sampleType', 'fieldSampleMass', 'specimenType', 'specimenMass',
    'specimenNumbers', 'specimenMaxGrainSize', 'specimenMaxGrainFraction',
    'schedulingNotes', 'testDescription'
  ]
};

const AddItem = () => {
  const navigate = useNavigate();

  const allPossibleFields = Array.from(new Set(Object.values(dynamicFields).flat()));
  const initialFormData = {};
  allPossibleFields.forEach(field => {
    initialFormData[field] = '';
  });

  const [formData, setFormData] = useState(initialFormData);
  const [databaseTarget, setDatabaseTarget] = useState('database');
  const [showConfirm, setShowConfirm] = useState(false);
  const [showPasswordPrompt, setShowPasswordPrompt] = useState(false);
  const [password, setPassword] = useState('');
  const [imageFile, setImageFile] = useState(null);
  const [imageURL, setImageURL] = useState('');
  const [isAuthorized, setIsAuthorized] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) return setIsAuthorized(false);
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const roles = payload?.roles || payload?.authorities || [];
      const sub = payload?.sub;
      const isAdmin =
        (Array.isArray(roles) && (roles.includes('ROLE_ADMIN') || roles.includes('ADMIN'))) ||
        (typeof roles === 'string' && roles.includes('ADMIN')) ||
        sub === 'admin';
      setIsAuthorized(isAdmin);
    } catch (err) {
      console.error('Token parsing error:', err);
      setIsAuthorized(false);
    }
  }, []);

  useEffect(() => {
    const fieldsForTarget = dynamicFields[databaseTarget] || [];
    setFormData(prevFormData => {
      const newFormData = { ...prevFormData };
      fieldsForTarget.forEach(f => {
        newFormData[f] = '';
      });
      return newFormData;
    });
    setImageFile(null);
    setImageURL('');
  }, [databaseTarget]);

  if (isAuthorized === false) {
    return (
      <div className="edit-item-container">
        <h2>403 Forbidden</h2>
        <p>You are not allowed to add an item to this database.</p>
        <p>If you believe this is an error, please contact an administrator.</p>
      </div>
    );
  }

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    setImageFile(file);
    setImageURL(file ? URL.createObjectURL(file) : '');
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
    try {
      const authResponse = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          username: localStorage.getItem('username'),
          password
        })
      });
      const authData = await authResponse.json();
      if (!authResponse.ok || !authData.token) {
        throw new Error('Authentication failed: incorrect password or missing token');
      }

      const token = authData.token;
      localStorage.setItem('token', token);

      // Filter only relevant fields with non-empty values
      const fieldsForTarget = dynamicFields[databaseTarget] || [];
      const filteredData = {};
      fieldsForTarget.forEach(f => {
        if (formData[f] !== '' && formData[f] != null) {
          filteredData[f] = formData[f];
        }
      });
      filteredData.databaseBelongsTo = databaseTarget;

      const url = `http://localhost:8080/${databaseTarget}/add`;
      const formDataToSend = new FormData();
      formDataToSend.append('data', JSON.stringify(filteredData));
      if (imageFile) formDataToSend.append('image', imageFile);

      const addResponse = await fetch(url, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
        },
        body: formDataToSend
      });

      if (!addResponse.ok) {
        if (addResponse.status === 403) {
          alert('403 Forbidden: You are not authorized to add this resource.');
          return;
        }
        throw new Error(`Failed to add item: ${addResponse.status}`);
      }

      alert('Item added successfully!');
      navigate('/');
    } catch (error) {
      console.error(error);
      alert(error.message);
      setShowPasswordPrompt(false);
    }
  };

  const formatLabel = (label) =>
    label.replace(/([A-Z])/g, ' $1').replace(/^./, str => str.toUpperCase());

  if (isAuthorized === null) return null;

  const fields = dynamicFields[databaseTarget] || [];

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
            <option value="inSituTest">In Situ</option>
            <option value="earthworks">Earthworks</option>
          </select>
        </div>

        {fields.map((field, idx) => (
          <div className="form-row" key={field}>
            <label htmlFor={field}>{formatLabel(field)}:</label>
            <input
              id={field}
              name={field}
              value={formData[field] || ''}
              placeholder="None"
              onChange={handleChange}
              autoFocus={idx === 0}
            />
          </div>
        ))}

        {imageURL && (
          <div className="form-row">
            <label>Image Preview:</label>
            <img src={imageURL} alt="Preview" style={{ maxWidth: '200px', maxHeight: '200px' }} />
          </div>
        )}

        <div className="form-row">
          <label htmlFor="image">Upload Image (optional):</label>
          <input type="file" id="image" accept="image/*" onChange={handleImageChange} />
        </div>

        <div className="button-group">
          <button type="submit">Submit</button>
          <button type="button" onClick={() => navigate(-1)}>Cancel</button>
        </div>
      </form>

      {showConfirm && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h3>Confirm adding new item?</h3>
            <table>
              <tbody>
                {fields.map(f => (
                  <tr key={f}>
                    <td><strong>{formatLabel(f)}:</strong></td>
                    <td>{formData[f]}</td>
                  </tr>
                ))}
                {imageFile && (
                  <tr>
                    <td><strong>Image File:</strong></td>
                    <td>{imageFile.name}</td>
                  </tr>
                )}
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
            <h3>Enter Password to Confirm</h3>
            <input
              type="password"
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
