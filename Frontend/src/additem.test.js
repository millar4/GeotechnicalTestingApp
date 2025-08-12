import React from 'react';
import { render, screen, fireEvent, waitFor, within } from '@testing-library/react';
import AddItem from './additem';
import { MemoryRouter } from 'react-router-dom';

const mockNavigate = jest.fn();

jest.mock('react-router-dom', () => {
  const originalModule = jest.requireActual('react-router-dom');
  return {
    __esModule: true,
    ...originalModule,
    useNavigate: () => mockNavigate,
  };
});

beforeEach(() => {
  localStorage.clear();
  localStorage.setItem('token', 'fake_token');
  localStorage.setItem('username', 'testuser');
});

beforeEach(() => {
  global.fetch = jest.fn((url, options) => {
    if (url.includes('/api/auth/login')) {
      return Promise.resolve({
        ok: true,
        status: 200,
        json: () => Promise.resolve({ token: 'fake_token' }),
      });
    }
    if (url.includes('/database/add') || url.includes('/aggregate/add') || url.includes('/rocks/add')) {
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

function renderAddItem() {
  return render(
    <MemoryRouter>
      <AddItem />
    </MemoryRouter>
  );
}

describe('AddItem Component Tests', () => {
  test('renders initial form fields and heading', () => {
    renderAddItem();
    expect(screen.getByRole('heading', { name: /Add New Item/i })).toBeInTheDocument();
    expect(screen.getByLabelText('Target Database:')).toBeInTheDocument();
    expect(screen.getByLabelText('Test:')).toBeInTheDocument();
  });

  test('shows confirmation modal on form submission', async () => {
    renderAddItem();
    const testInput = screen.getByLabelText('Test:');
    fireEvent.change(testInput, { target: { value: 'New Test' } });
    const submitButton = screen.getByRole('button', { name: /^Submit$/i });
    fireEvent.submit(submitButton.closest('form'));
    await waitFor(() => {
      expect(screen.getByText(/Are you sure to add a new item\?/i)).toBeInTheDocument();
    });
  });

  test('transitions from confirmation modal to password prompt modal on confirm', async () => {
    renderAddItem();
    const testInput = screen.getByLabelText('Test:');
    fireEvent.change(testInput, { target: { value: 'New Test' } });
    const submitButton = screen.getByRole('button', { name: /^Submit$/i });
    fireEvent.submit(submitButton.closest('form'));
    await waitFor(() => {
      expect(screen.getByText(/Are you sure to add a new item\?/i)).toBeInTheDocument();
    });
    const confirmButton = screen.getByRole('button', { name: /^Confirm$/i });
    fireEvent.click(confirmButton);
    await waitFor(() => {
      expect(screen.getByText(/Please enter your password to confirm/i)).toBeInTheDocument();
    });
  });

  test('final submission adds item successfully and navigates home', async () => {
    const alertSpy = jest.spyOn(window, 'alert').mockImplementation(() => {});
    renderAddItem();
    const testInput = screen.getByLabelText('Test:');
    fireEvent.change(testInput, { target: { value: 'New Test' } });
    const submitButton = screen.getByRole('button', { name: /^Submit$/i });
    fireEvent.submit(submitButton.closest('form'));
    await waitFor(() => {
      expect(screen.getByText(/Are you sure to add a new item\?/i)).toBeInTheDocument();
    });
    const confirmButton = screen.getByRole('button', { name: /^Confirm$/i });
    fireEvent.click(confirmButton);
    await waitFor(() => {
      expect(screen.getByText(/Please enter your password to confirm/i)).toBeInTheDocument();
    });
    const passwordInput = screen.getByPlaceholderText('Enter password');
    fireEvent.change(passwordInput, { target: { value: 'correctpassword' } });
    const modalContent = screen.getByText(/Please enter your password to confirm/i).closest('.modal-content');
    const finalSubmitButton = within(modalContent).getByRole('button', { name: /^Submit$/i });
    fireEvent.click(finalSubmitButton);
    await waitFor(() => {
      expect(alertSpy).toHaveBeenCalledWith('Item successfully added!');
      expect(mockNavigate).toHaveBeenCalledWith('/');
    });
    alertSpy.mockRestore();
  });
});




