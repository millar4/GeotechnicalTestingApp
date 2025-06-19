// AccountManagement.test.js
import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import AccountManagement from './AccountManagement';
import { BrowserRouter } from 'react-router-dom';

// Setup localStorage before each test
beforeEach(() => {
  localStorage.clear();
  localStorage.setItem('token', 'fake_token');
  localStorage.setItem('userId', '1'); // For change password test
});

// Mock fetch for all endpoints used by AccountManagement
beforeEach(() => {
  global.fetch = jest.fn((url, options) => {
    if (url.endsWith('/admin/users')) {
      return Promise.resolve({
        ok: true,
        status: 200,
        json: () =>
          Promise.resolve([
            { id: 1, username: 'admin', role: 'ADMIN' },
            { id: 2, username: 'user1', role: 'USER' }
          ]),
      });
    }
    if (url.endsWith('/admin/createUser')) {
      return Promise.resolve({
        ok: true,
        status: 200,
        text: () => Promise.resolve('User created successfully!'),
      });
    }
    if (url.includes('/admin/deleteUser/')) {
      return Promise.resolve({
        ok: true,
        status: 200,
        text: () => Promise.resolve('User deleted successfully.'),
      });
    }
    if (url.includes('/admin/updateUser/')) {
      return Promise.resolve({
        ok: true,
        status: 200,
        text: () => Promise.resolve('Success'),
      });
    }
    return Promise.resolve({
      ok: true,
      status: 200,
      json: () => Promise.resolve({}),
    });
  });
});

afterEach(() => {
  global.fetch.mockRestore();
});

// Helper function to render AccountManagement with Router
function renderAccountManagement() {
  return render(
    <BrowserRouter>
      <AccountManagement />
    </BrowserRouter>
  );
}

describe('AccountManagement Component Tests', () => {
  test('renders account management headings and sections', async () => {
    renderAccountManagement();

    // Use getByRole to ensure we only match headings (h1, h2, etc.)
    expect(screen.getByRole('heading', { name: /Account Management/i })).toBeInTheDocument();
    expect(screen.getByRole('heading', { name: /Add New Account/i })).toBeInTheDocument();
    expect(screen.getByRole('heading', { name: /Existing Accounts/i })).toBeInTheDocument();
    expect(screen.getByRole('heading', { name: /Change Password/i })).toBeInTheDocument();
  });

  test('submits new account form successfully', async () => {
    const alertSpy = jest.spyOn(window, 'alert').mockImplementation(() => {});
    renderAccountManagement();

    // Fill in the "Add New Account" form fields
    const usernameInput = screen.getByPlaceholderText(/Enter username/i);
    const passwordInput = screen.getByPlaceholderText(/Enter password/i);
    const roleSelect = screen.getByDisplayValue(/User/i);

    fireEvent.change(usernameInput, { target: { value: 'newuser' } });
    fireEvent.change(passwordInput, { target: { value: 'newpassword' } });
    fireEvent.change(roleSelect, { target: { value: 'Admin' } });

    const addAccountButton = screen.getByRole('button', { name: /Add Account/i });
    fireEvent.click(addAccountButton);

    await waitFor(() => {
      expect(alertSpy).toHaveBeenCalledWith('User created successfully!');
    });

    alertSpy.mockRestore();
  });

  test('submits change password form successfully', async () => {
    const alertSpy = jest.spyOn(window, 'alert').mockImplementation(() => {});
    renderAccountManagement();

    // Fill in the "Change Password" form fields
    const currentPasswordInput = screen.getByPlaceholderText(/Current password/i);
    // Use an exact match to distinguish the "New password" field
    const newPasswordInput = screen.getByPlaceholderText('New password');
    const confirmNewPasswordInput = screen.getByPlaceholderText(/Confirm new password/i);

    fireEvent.change(currentPasswordInput, { target: { value: 'oldpass' } });
    fireEvent.change(newPasswordInput, { target: { value: 'newpass' } });
    fireEvent.change(confirmNewPasswordInput, { target: { value: 'newpass' } });

    const changePasswordButton = screen.getByRole('button', { name: /Change Password/i });
    fireEvent.click(changePasswordButton);

    await waitFor(() => {
      expect(alertSpy).toHaveBeenCalledWith('Password changed successfully!');
    });

    alertSpy.mockRestore();
  });
});
