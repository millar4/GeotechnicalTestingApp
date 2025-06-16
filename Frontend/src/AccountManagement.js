import React, { useEffect, useState } from 'react';
import './AccountManagement.css';

function AccountManagement() {
  const [accounts, setAccounts] = useState([]);
  
  // Add new user form
  const [newUsername, setNewUsername] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [newRole, setNewRole] = useState('User');
  
  // Change password form
  const [currentPassword, setCurrentPassword] = useState('');
  const [newPasswordInput, setNewPasswordInput] = useState('');
  const [confirmNewPassword, setConfirmNewPassword] = useState('');
  
  // Get the JWT token of the currently logged in user from localStorage.
  // (assuming the token was saved to localStorage when the login succeeded)
  const token = localStorage.getItem('token') || '';

  // =============== 1. Get the list of users on initial load ===============
  useEffect(() => {
    fetchAccounts();
  }, []);

  const fetchAccounts = async () => {
    try {
      const response = await fetch('http://localhost:8080/admin/users', {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      if (!response.ok) {
        throw new Error('Failed to fetch user list');
      }
      const data = await response.json();
      // As required: Admin users are first, User users are second.
      // Simple: sort the data first
      const sorted = [...data].sort((a, b) => {
        if (a.role === b.role) {
          return a.username.localeCompare(b.username);
        }
        return a.role === 'ADMIN' ? -1 : 1;
      });
      setAccounts(sorted);
    } catch (err) {
      console.error(err);
      alert('Error fetching user list: ' + err.message);
    }
  };

  // =============== 2. Add new account ===============
  const handleAddAccount = async (e) => {
    e.preventDefault();
    if (!newUsername || !newPassword) {
      alert('Username and password cannot be empty.');
      return;
    }

    try {
      const response = await fetch('http://localhost:8080/admin/createUser', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
          username: newUsername,
          password: newPassword,
          role: newRole.toUpperCase()
        })
      });
      if (!response.ok) {
        const text = await response.text();
        throw new Error(text);
      }
      alert('User created successfully!');
      setNewUsername('');
      setNewPassword('');
      setNewRole('User');
      // Refresh the user list
      fetchAccounts();
    } catch (err) {
      console.error(err);
      alert('Failed to create user: ' + err.message);
    }
  };

  // =============== 3. Delete account ===============
  const handleDeleteAccount = async (accountId) => {
    if (!window.confirm('Are you sure you want to delete this user?')) {
      return;
    }
    try {
      const response = await fetch(`http://localhost:8080/admin/deleteUser/${accountId}`, {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      if (!response.ok) {
        const text = await response.text();
        throw new Error(text);
      }
      alert('User deleted successfully.');
      fetchAccounts();
    } catch (err) {
      console.error(err);
      alert('Failed to delete user: ' + err.message);
    }
  };

  // =============== 4. Lifting or downgrading (switching roles) ===============
  const handleToggleRole = async (account) => {
    const newRole = account.role === 'ADMIN' ? 'USER' : 'ADMIN';
    if (!window.confirm(`Change role of ${account.username} to ${newRole}?`)) {
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/admin/updateUser/${account.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
          username: account.username, 
          password: '', 
          role: newRole 
        })
      });
      if (!response.ok) {
        const text = await response.text();
        throw new Error(text);
      }
      alert(`Role changed to ${newRole} successfully.`);
      fetchAccounts();
    } catch (err) {
      console.error(err);
      alert('Failed to change user role: ' + err.message);
    }
  };

   // =============== 5. Change password ===============
  // Ask the user to enter “current password”, “new password”, “confirm new password”.
  // Usually the backend needs to verify that the current password is correct.
  // Assuming the backend handles the old password verification in the same updateUser interface
  // and only allows you to change it yourself (or an administrator to force it).

  const handleChangePassword = async (e) => {
    e.preventDefault();
    if (!currentPassword || !newPasswordInput) {
      alert('Current and new password must not be empty.');
      return;
    }
    if (newPasswordInput !== confirmNewPassword) {
      alert('New password and confirm password do not match.');
      return;
    }

    const currentUserId = localStorage.getItem('userId'); 
    if (!currentUserId) {
      alert('No current user id found in localStorage. Please adjust logic as needed.');
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/admin/updateUser/${currentUserId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
          username: '', 
     // pass the old password + the new password, the backend must check that the old password is correct
          oldPassword: currentPassword,
          password: newPasswordInput,
          role: '' 
        })
      });
      if (!response.ok) {
        const text = await response.text();
        throw new Error(text);
      }
      alert('Password changed successfully!');
      setCurrentPassword('');
      setNewPasswordInput('');
      setConfirmNewPassword('');
    } catch (err) {
      console.error(err);
      alert('Failed to change password: ' + err.message);
    }
  };

  return (
    <div className="container">
      <div className="account-management">
        <h1>Account Management</h1>

        {/* ========== Add new account ========== */}
        <section className="add-account">
          <h2>Add New Account</h2>
          <form onSubmit={handleAddAccount}>
            <div className="form-group">
              <label>Username:</label>
              <input 
                type="text" 
                placeholder="Enter username" 
                value={newUsername}
                onChange={(e) => setNewUsername(e.target.value)}
                required 
              />
            </div>
            <div className="form-group">
              <label>Initial Password:</label>
              <input 
                type="password" 
                placeholder="Enter password" 
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                required 
              />
            </div>
            <div className="form-group">
              <label>Role:</label>
              <select 
                value={newRole}
                onChange={(e) => setNewRole(e.target.value)}
                required
              >
                <option value="User">User</option>
                <option value="Admin">Admin</option>
              </select>
            </div>
            <button type="submit" className="btn">Add Account</button>
          </form>
        </section>

        {/* ========== list of users ========== */}
        <section className="account-list">
          <h2>Existing Accounts</h2>
          <ul>
            {accounts.map((account) => (
              <li key={account.id}>
                <span>{account.username} - {account.role}</span>
                &nbsp;
                {/* Delete */}
                <button 
                  onClick={() => handleDeleteAccount(account.id)} 
                  className="btn"
                >
                  Delete
                </button>
                &nbsp;
                {/* Switching roles => uplift/downlift */}
                <button 
                  onClick={() => handleToggleRole(account)}
                  className="btn"
                >
                  {account.role === 'ADMIN' ? 'Demote to User' : 'Promote to Admin'}
                </button>
              </li>
            ))}
          </ul>
        </section>

        {/* ========== change password ========== */}
        <section className="change-password">
          <h2>Change Password</h2>
          <form onSubmit={handleChangePassword}>
            <div className="form-group">
              <label>Current Password:</label>
              <input
                type="password"
                placeholder="Current password"
                value={currentPassword}
                onChange={(e) => setCurrentPassword(e.target.value)}
                required
              />
            </div>
            <div className="form-group">
              <label>New Password:</label>
              <input
                type="password"
                placeholder="New password"
                value={newPasswordInput}
                onChange={(e) => setNewPasswordInput(e.target.value)}
                required
              />
            </div>
            <div className="form-group">
              <label>Confirm New Password:</label>
              <input
                type="password"
                placeholder="Confirm new password"
                value={confirmNewPassword}
                onChange={(e) => setConfirmNewPassword(e.target.value)}
                required
              />
            </div>
            <button type="submit" className="btn">Change Password</button>
          </form>
        </section>
      </div>
    </div>
  );
}

export default AccountManagement;