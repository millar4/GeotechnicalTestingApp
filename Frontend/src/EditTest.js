import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation, useParams } from 'react-router-dom';
import './EditTest.css';

const EditTest = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { targetDatabase } = useParams();
  const state = location.state;
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
  const [imageFile, setImageFile] = useState(null);
  const [imageURL, setImageURL] = useState(
    initialData.imagePath ? `http://localhost:8080/${initialData.imagePath}` : ''
  );
  const [showPasswordPrompt, setShowPasswordPrompt] = useState(false);
  const [password, setPassword] = useState('');
  const [actionType, setActionType] = useState('');
  const [isAuthorized, setIsAuthorized] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    setImageFile(file);
    setImageURL(URL.createObjectURL(file));
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
      // Authenticate user
      const authResponse = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: currentUsername, password })
      });

      if (!authResponse.ok) throw new Error('Authentication failed: incorrect password');

      // Update or delete request
      if (actionType === 'update') {
        const formDataToSend = new FormData();
        formDataToSend.append('data', JSON.stringify(formData));
        if (imageFile) formDataToSend.append('image', imageFile);

        const updateResponse = await fetch(
          `http://localhost:8080/${targetDatabase}/update-with-image/${testId}`,
          {
            method: 'PUT',
            headers: { 'Authorization': `Bearer ${token}` },
            body: formDataToSend
          }
        );

        if (!updateResponse.ok) {
          if (updateResponse.status === 403) throw new Error('403 Forbidden: Not authorized.');
          throw new Error(`Failed to update item: ${updateResponse.status}`);
        }

        alert('Test updated successfully!');
      } else if (actionType === 'delete') {
        const deleteResponse = await fetch(
          `http://localhost:8080/${targetDatabase}/delete/${testId}`,
          {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
          }
        );

        if (!deleteResponse.ok) {
          if (deleteResponse.status === 403) throw new Error('403 Forbidden: Not authorized.');
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

      // ✅ Persist selected database so AllTestListPage fetches correct data
      const dbType = formData.databaseBelongsTo || targetDatabase;
      localStorage.setItem('lastDatabase', dbType);

      // Navigate to AllTestListPage
      navigate('/AllTestListPage');
    } catch (error) {
      console.error(error);
      alert(error.message);
    } finally {
      setShowPasswordPrompt(false);
    }
  };

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) return setIsAuthorized(false);

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const roles = payload?.roles || payload?.authorities || [];
      const sub = payload?.sub;

      const isAdmin = Array.isArray(roles)
        ? roles.includes('ROLE_ADMIN') || roles.includes('ADMIN')
        : typeof roles === 'string'
          ? roles.includes('ADMIN')
          : false;

      const isAdminUser = isAdmin || sub === 'admin';
      setIsAuthorized(isAdminUser);
    } catch (err) {
      console.error('Token decoding error:', err);
      setIsAuthorized(false);
    }
  }, []);

  const dynamicFields = {
    aggregate: ['test','group','classification','symbol','parameters','testMethod','sampleType','fieldSampleMass','specimenType','specimenMass','specimenNumbers','specimenMaxGrainSize','specimenMaxGrainFraction','schedulingNotes','testDescription'],
    rock: ['test','group','classification','symbol','parameters','testMethod','alt1','alt2','alt3','sampleType','fieldSampleType','specimenType','specimenMass','specimenNumbers','specimenD','specimenL','specimenW','specimenH','specimenMaxGrainSize','specimenMaxGrainFraction','schedulingNotes','testDescription'],
    concrete: ['test','group','classification','symbol','parameters','testMethod','alt1','alt2','alt3','sampleType','fieldSampleMass','specimenType','specimenMass','specimenNumbers','specimenD','specimenL','specimenW','specimenH','specimenMaxGrainSize','specimenMaxGrainFraction','schedulingNotes','testDescription'],
    database: ['test','group','classification','symbol','parameters','testMethod','alt1','alt2','alt3','sampleType','fieldSampleMass','specimenType','specimenMass','specimenNumbers','specimenD','specimenL','specimenW','specimenH','specimenMaxGrainSize','specimenMaxGrainFraction','testDescription'],
    inSituTest: ['test','group','classification','symbol','parameters','testMethod','alt1','alt2','alt3','sampleType','materials','applications','testDescription'],
    earthworks: ['test','group','classification','symbol','parameters','testMethod','sampleType','fieldSampleMass','specimenType','specimenMass','specimenNumbers','specimenMaxGrainSize','specimenMaxGrainFraction','schedulingNotes','testDescription']
  };

  const selectedFields = dynamicFields[targetDatabase] || dynamicFields.aggregate;

  const fieldLabels = {
    test: 'Test Name',
    group: 'Test Group',
    classification: 'UKSGI (3rd ed.) BOQ No.',
    symbol: 'Symbol',
    parameters: 'Parameters',
    testMethod: 'Primary Test Method',
    alt1: 'Alternative Method 1',
    alt2: 'Alternative Method 2',
    alt3: 'Alternative Method 3',
    sampleType: 'Sample Condition',
    fieldSampleMass: 'Field Sample Mass (kg)',
    specimenType: 'Specimen Condition',
    specimenMass: 'Specimen Mass (kg)',
    specimenNumbers: 'Number of Specimens',
    specimenD: 'Specimen Diameter (mm)',
    specimenL: 'Specimen Length (mm)',
    specimenW: 'Specimen Width (mm)',
    specimenH: 'Specimen Height (mm)',
    specimenMaxGrainSize: 'Max Particle Size',
    specimenMaxGrainFraction: 'Grain Fraction Used (mm)',
    schedulingNotes: 'Scheduling Notes',
    materials: 'Materials',
    applications: 'Applications',
    testDescription: 'Test Description'
  };

  if (isAuthorized === false) {
    return (
      <div className="edit-item-container">
        <h2>403 - Forbidden</h2>
        <p>Resource not found or you do not have permission to access this test.</p>
        <button onClick={() => navigate('/')}>Go to Home</button>
      </div>
    );
  }

  if (isAuthorized === null) return null;

  return (
    <div className="edit-item-container">
      <h2>Edit Test</h2>
      <form onSubmit={handleSubmit}>
        {selectedFields.map(field => (
          <div className="form-row" key={field}>
            <label htmlFor={field}>{fieldLabels[field] || field}:</label>
            {field === 'testDescription' || field === 'schedulingNotes' ? (
              <textarea
                id={field}
                name={field}
                value={formData[field] || ''}
                onChange={handleChange}
                rows={4}
              />
            ) : (
              <input
                id={field}
                name={field}
                value={formData[field] || ''}
                onChange={handleChange}
              />
            )}
          </div>
        ))}

        {imageURL && (
          <div className="form-row">
            <label>Current Image:</label>
            <img src={imageURL} alt="Current" style={{ width: '200px', height: 'auto' }} />
          </div>
        )}

        <div className="form-row">
          <label htmlFor="image">Upload Image (optional):</label>
          <input type="file" id="image" accept="image/*" onChange={handleImageChange} />
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
              Enter password to confirm {actionType === 'update' ? 'update' : 'deletion'}
            </h3>
            <input
              type="password"
              placeholder="Password"
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
