import React, { useEffect, useState } from 'react';
import './AccountManagement.css';

function AccountManagement() {
  const [accounts, setAccounts] = useState([]);

  const [newUsername, setNewUsername] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [newRole, setNewRole] = useState('User');

  const [currentPassword, setCurrentPassword] = useState('');
  const [newPasswordInput, setNewPasswordInput] = useState('');
  const [confirmNewPassword, setConfirmNewPassword] = useState('');

  const token = localStorage.getItem('token') || '';
  const currentUserId = localStorage.getItem('userId');

  useEffect(() => {
    fetchAccounts();
  }, []);

  const fetchAccounts = async () => {
    try {
      const response = await fetch('http://localhost:8080/admin/users', {
        headers: { 'Authorization': `Bearer ${token}` },
      });
      if (!response.ok) throw new Error('Failed to fetch users');
      const data = await response.json();
      const sorted = data.sort((a, b) =>
        a.role === b.role ? a.username.localeCompare(b.username) : a.role === 'ADMIN' ? -1 : 1
      );
      setAccounts(sorted);
    } catch (err) {
      console.error(err);
      alert('Error fetching users: ' + err.message);
    }
  };

  const handleAddAccount = async (e) => {
    e.preventDefault();
    if (!newUsername || !newPassword) return alert('Username and password required.');

    try {
      const response = await fetch('http://localhost:8080/admin/createUser', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({
          username: newUsername,
          password: newPassword,
          role: newRole.toUpperCase(),
        }),
      });
      if (!response.ok) throw new Error(await response.text());

      alert('User created.');
      setNewUsername('');
      setNewPassword('');
      setNewRole('User');
      fetchAccounts();
    } catch (err) {
      alert('Error: ' + err.message);
    }
  };

  const handleDeleteAccount = async (id) => {
    if (!window.confirm('Delete this user?')) return;
    try {
      const response = await fetch(`http://localhost:8080/admin/deleteUser/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${token}` },
      });
      if (!response.ok) throw new Error(await response.text());

      alert('User deleted.');
      fetchAccounts();
    } catch (err) {
      alert('Error: ' + err.message);
    }
  };

  const handleToggleRole = async (account) => {
    const updatedRole = account.role === 'ADMIN' ? 'USER' : 'ADMIN';
    if (!window.confirm(`Change role to ${updatedRole}?`)) return;

    try {
      const response = await fetch(`http://localhost:8080/admin/updateUser/${account.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({
          username: account.username,
          password: '',
          role: updatedRole,
        }),
      });
      if (!response.ok) throw new Error(await response.text());

      alert('Role updated.');
      fetchAccounts();
    } catch (err) {
      alert('Error: ' + err.message);
    }
  };

  const handleChangePassword = async (e) => {
    e.preventDefault();
    if (!currentPassword || !newPasswordInput) return alert('Fill all fields.');
    if (newPasswordInput !== confirmNewPassword) return alert('Passwords do not match.');
    if (!currentUserId) return alert('Missing user ID.');

    try {
      const response = await fetch(`http://localhost:8080/admin/updateUser/${currentUserId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({
          username: '',
          oldPassword: currentPassword,
          password: newPasswordInput,
          role: '',
        }),
      });
      if (!response.ok) throw new Error(await response.text());

      alert('Password updated.');
      setCurrentPassword('');
      setNewPasswordInput('');
      setConfirmNewPassword('');
    } catch (err) {
      alert('Error: ' + err.message);
    }
  };

  return (
    <div className="account-container">
      <h1>Account Management</h1>

      {/* Add New User */}
      <div className="card spaced-section">
        <h2>Add New User</h2>
        <form onSubmit={handleAddAccount} className="form-vertical">
          <input
            type="text"
            placeholder="Username"
            value={newUsername}
            onChange={(e) => setNewUsername(e.target.value)}
            required
            className="input-field"
          />
          <input
            type="password"
            placeholder="Password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            required
            className="input-field"
          />
          <select
            value={newRole}
            onChange={(e) => setNewRole(e.target.value)}
            className="input-field"
          >
            <option value="User">User</option>
            <option value="Admin">Admin</option>
          </select>
          <button type="submit" className="btn">Add</button>
        </form>
      </div>

      {/* List Users */}
      <div className="card spaced-section">
        <h2>Existing Users</h2>
        {accounts.length === 0 ? (
          <p>No users found.</p>
        ) : (
          <ul className="user-list">
            {accounts.map((acc) => (
              <li key={acc.id}>
                <div className="user-info">
                  <strong>{acc.username}</strong> <em>({acc.role})</em>
                </div>
                <div className="actions">]
                  <button onClick={() => handleDeleteAccount(acc.id)} className="btn small">Delete</button>
                  <button onClick={() => handleToggleRole(acc)} className="btn small">
                    {acc.role === 'ADMIN' ? 'Demote' : 'Promote'}
                  </button>
                </div>
              </li>
            ))}
          </ul>
        )}
      </div>

      {/* Change Password */}
      <div className="card spaced-section">
        <h2>Change Your Password</h2>
        <form onSubmit={handleChangePassword} className="form-vertical">
          <input
            type="password"
            placeholder="Current password"
            value={currentPassword}
            onChange={(e) => setCurrentPassword(e.target.value)}
            required
            className="input-field"
          />
          <input
            type="password"
            placeholder="New password"
            value={newPasswordInput}
            onChange={(e) => setNewPasswordInput(e.target.value)}
            required
            className="input-field"
          />
          <input
            type="password"
            placeholder="Confirm new password"
            value={confirmNewPassword}
            onChange={(e) => setConfirmNewPassword(e.target.value)}
            required
            className="input-field"
          />
          <button type="submit" className="btn">Change Password</button>
        </form>
      </div>
    </div>
  );
}

export default AccountManagement;
