// EditTest.test.js
import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import EditTest from './EditTest';
import { MemoryRouter } from 'react-router-dom';

// Mock react-router-dom hooks
jest.mock('react-router-dom', () => {
  const originalModule = jest.requireActual('react-router-dom');
  return {
    __esModule: true,
    ...originalModule,
    useNavigate: () => jest.fn(), // default mock (we override in tests if needed)
    useLocation: () => ({
      state: {
        details: {
          id: 1,
          test: 'Initial Test',
          group: 'Group A',
          symbol: 'T',
          parameters: 'Param',
          testMethod: 'Method',
          alt1: 'A1',
          alt2: 'A2',
          alt3: 'A3',
          sampleType: 'Sample',
          fieldSampleMass: '50',
          specimenType: 'Type',
          specimenMass: '10',
          specimenNumbers: '2',
          specimenD: 'D',
          specimenL: 'L',
          specimenW: 'W',
          specimenH: 'H',
          specimenMaxGrainSize: 'Small',
          specimenMaxGrainFraction: '0.5',
          username: 'testuser'
        }
      }
    }),
  };
});

// Set up localStorage for tests
beforeEach(() => {
  localStorage.clear();
  localStorage.setItem('token', 'fake_token');
  localStorage.setItem('username', 'testuser'); // for update test scenario (self-deletion check)
});

// Mock global.fetch to handle authentication, update, and delete endpoints
beforeEach(() => {
  global.fetch = jest.fn((url, options) => {
    if (url.includes('/api/auth/login')) {
      return Promise.resolve({
        ok: true,
        status: 200,
        json: () => Promise.resolve({ token: 'fake_token' }),
      });
    }
    if (url.includes('/database/update/')) {
      return Promise.resolve({
        ok: true,
        status: 200,
        text: () => Promise.resolve('Success'),
      });
    }
    if (url.includes('/database/delete/')) {
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
  jest.clearAllMocks();
});

// Helper to render the component wrapped in MemoryRouter
function renderEditTest() {
  return render(
    <MemoryRouter>
      <EditTest />
    </MemoryRouter>
  );
}

describe('EditTest Component Tests', () => {
  test('renders initial form fields with provided details', () => {
    renderEditTest();

    // Verify that the form inputs have initial values from the location state.
    const testInput = screen.getByLabelText(/Test:/i);
    expect(testInput).toHaveValue('Initial Test');

    const groupInput = screen.getByLabelText(/Group:/i);
    expect(groupInput).toHaveValue('Group A');
  });

  test('shows password prompt when submitting update', async () => {
    renderEditTest();

    // Simulate form submission by clicking the "Update" button.
    const updateButton = screen.getByRole('button', { name: /^Update$/i });
    // Submit the form by firing a submit event on the form element.
    fireEvent.submit(updateButton.closest('form'));

    // Wait for the modal (password prompt) to appear.
    await waitFor(() => {
      expect(
        screen.getByText(/Please enter your password to confirm update/i)
      ).toBeInTheDocument();
    });
  });

  test('shows password prompt when clicking delete', async () => {
    renderEditTest();

    // Click the "Delete" button.
    const deleteButton = screen.getByRole('button', { name: /^Delete$/i });
    fireEvent.click(deleteButton);

    // Wait for the modal to appear with deletion prompt.
    await waitFor(() => {
      expect(
        screen.getByText(/Please enter your password to confirm deletion/i)
      ).toBeInTheDocument();
    });
  });

  test('final submit updates test successfully and navigates back', async () => {
    // Set up a spy for window.alert.
    const alertSpy = jest.spyOn(window, 'alert').mockImplementation(() => {});

    // Override useNavigate to capture navigation.
    const navigateMock = jest.fn();
    jest.spyOn(require('react-router-dom'), 'useNavigate').mockImplementation(() => navigateMock);

    renderEditTest();

    // Simulate form submission (update)
    const updateButton = screen.getByRole('button', { name: /^Update$/i });
    fireEvent.submit(updateButton.closest('form'));

    // Wait for the update password prompt to appear.
    await waitFor(() => {
      expect(
        screen.getByText(/Please enter your password to confirm update/i)
      ).toBeInTheDocument();
    });

    // Fill in the password in the modal.
    const passwordInput = screen.getByPlaceholderText(/Enter password/i);
    fireEvent.change(passwordInput, { target: { value: 'correctpassword' } });

    // Click the Confirm button.
    const confirmButton = screen.getByRole('button', { name: /^Confirm$/i });
    fireEvent.click(confirmButton);

    // Wait for the alert and navigation to be triggered.
    await waitFor(() => {
      expect(alertSpy).toHaveBeenCalledWith('Test updated successfully!');
      expect(navigateMock).toHaveBeenCalledWith(-1);
    });

    alertSpy.mockRestore();
  });

  test('final submit deletes test successfully and navigates back', async () => {
    // For delete testing, override the location state so that username is different (to avoid self-deletion confirmation).
    jest.spyOn(require('react-router-dom'), 'useLocation').mockImplementation(() => ({
      state: {
        details: {
          id: 1,
          test: 'Initial Test',
          group: 'Group A',
          symbol: 'T',
          parameters: 'Param',
          testMethod: 'Method',
          alt1: 'A1',
          alt2: 'A2',
          alt3: 'A3',
          sampleType: 'Sample',
          fieldSampleMass: '50',
          specimenType: 'Type',
          specimenMass: '10',
          specimenNumbers: '2',
          specimenD: 'D',
          specimenL: 'L',
          specimenW: 'W',
          specimenH: 'H',
          specimenMaxGrainSize: 'Small',
          specimenMaxGrainFraction: '0.5',
          username: 'otheruser'
        }
      }
    }));

    const alertSpy = jest.spyOn(window, 'alert').mockImplementation(() => {});
    const navigateMock = jest.fn();
    jest.spyOn(require('react-router-dom'), 'useNavigate').mockImplementation(() => navigateMock);

    renderEditTest();

    // Click the Delete button.
    const deleteButton = screen.getByRole('button', { name: /^Delete$/i });
    fireEvent.click(deleteButton);

    await waitFor(() => {
      expect(
        screen.getByText(/Please enter your password to confirm deletion/i)
      ).toBeInTheDocument();
    });

    const passwordInput = screen.getByPlaceholderText(/Enter password/i);
    fireEvent.change(passwordInput, { target: { value: 'correctpassword' } });

    const confirmButton = screen.getByRole('button', { name: /^Confirm$/i });
    fireEvent.click(confirmButton);

    await waitFor(() => {
      expect(alertSpy).toHaveBeenCalledWith('Test deleted successfully!');
      expect(navigateMock).toHaveBeenCalledWith(-1);
    });

    alertSpy.mockRestore();
  });
});
