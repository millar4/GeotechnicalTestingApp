import React, { useState } from 'react';
import { useNavigate, useLocation, useParams } from 'react-router-dom';
import './EditTest.css';

const EditTest = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { targetDatabase } = useParams();  // Extract targetDatabase from URL
  const state = location.state;
  
  // Log for debugging
  console.log(location.state);
  console.log('targetDatabase:', targetDatabase);

  const initialData = state?.details || {};

  const [formData, setFormData] = useState({
    test: initialData.test || '',
    group: initialData.group || '',
    symbol: initialData.symbol || '',
    classification: initialData.classification || '',
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
    schedulingNotes: initialData.schedulingNotes || '',
    databaseBelongsTo: initialData.databaseBelongsTo || '',
    testDescription: initialData.testDescription || ''
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
        body: JSON.stringify({ username: currentUsername, password })
      });

      if (!authResponse.ok) throw new Error('Authentication failed: incorrect password');

      if (actionType === 'update') {
        console.log('Submitting update with:', formData);

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

const dynamicFields = {
  aggregate: [
    'test', 'group','classification', 'symbol', 'parameters', 'testMethod', 'alt1', 'alt2', 'alt3',
    'sampleType', 'fieldSampleMass', 'specimenType', 'specimenMass', 'specimenNumbers',
    'specimenD', 'specimenL', 'specimenW', 'specimenH', 'specimenMaxGrainSize', 'specimenMaxGrainFraction', 'testDescription'
  ],
  rock: [
    'test', 'group','classification','symbol', 'parameters', 'testMethod', 'alt1', 'alt2', 'alt3',
    'sampleType', 'fieldSampleType', 'specimenType', 'specimenMass', 'specimenNumbers',
    'specimenD', 'specimenL', 'specimenW', 'specimenH', 'specimenMaxGrainSize', 'specimenMaxGrainFraction',
    'schedulingNotes', 'testDescription'
  ],
  concrete: [
    'test', 'group', 'classification', 'symbol', 'parameters', 'testMethod', 'alt1', 'alt2', 'alt3',
    'sampleType', 'fieldSampleMass', 'specimenType', 'specimenMass', 'specimenNumbers',
    'specimenD', 'specimenL', 'specimenW', 'specimenH', 'specimenMaxGrainSize', 'specimenMaxGrainFraction',
    'schedulingNotes', 'testDescription'
  ],
  database: [
    'test', 'group', 'classification', 'symbol', 'parameters', 'testMethod', 'alt1', 'alt2', 'alt3',
    'sampleType', 'fieldSampleMass', 'specimenType', 'specimenMass', 'specimenNumbers',
    'specimenD', 'specimenL', 'specimenW', 'specimenH', 'specimenMaxGrainSize', 'specimenMaxGrainFraction', 'testDescription'
  ],
  inSituTest: [
    'test', 'group', 'classification','symbol', 'parameters', 'testMethod', 'alt1', 'alt2', 'alt3',
    'sampleType', 'materials', 'applications', 'testDescription'
  ],
  earthworks: [
    'test', 'group','classification', 'symbol', 'parameters', 'testMethod', 'alt1', 'alt2', 'alt3',
    'sampleType', 'materials', 'applications', 'testDescription'
  ]
};


  const selectedFields = dynamicFields[targetDatabase] || dynamicFields.aggregate;
  const fieldLabels = {
  test: 'Test Name',
  group: 'Test Group',
  classification: 'UKSGI (3rd ed.) BOQ No.',
  testMethod: 'Primary Test Method',
  alt1: 'Altenative Method 1',
  alt2: 'Alternative Method 2',
  alt3: 'Alternative Method 3',
  sampleType: 'Sample Condition',
  fieldSampleMass: 'Field Sample Mass required(kg)',
  specimenType: 'Specimen Condition',
  specimenMass: 'Specimen Mass required(kg)',
  specimenNumbers: 'Number of Specimens required',
  specimenD: 'Specimen Diameter (mm)',
  specimenL: 'Specimen Length (mm)',
  specimenW: 'Specimen Width (mm)',
  specimenH: 'Specimen Height (mm)',
  specimenMaxGrainSize: 'Maximum Particle Size',
  specimenMaxGrainFraction: 'Particle size fractions used in test (mm)',
  testDescription: 'Test Description: '
};

  return (
    <div className="edit-item-container">
      <h2>Edit Test</h2>
      <form onSubmit={handleSubmit}>
        {selectedFields.map(field => (
            <div className="form-row" key={field}>
              <label htmlFor={field}>{fieldLabels[field] || field.replace(/([A-Z])/g, ' $1')}:</label>
              
              {field === 'testDescription' ? (
                <textarea
                  id={field}
                  name={field}
                  value={formData[field] || ''}
                  onChange={handleChange}
                  placeholder={`Enter ${field.replace(/([A-Z])/g, ' $1')}`}
                  rows={6}
                  style={{ resize: 'vertical' }}
                />
              ) : (
                <input
                  id={field}
                  name={field}
                  value={formData[field] || ''}
                  onChange={handleChange}
                  placeholder={`Enter ${field.replace(/([A-Z])/g, ' $1')}`}
                />
              )}
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
