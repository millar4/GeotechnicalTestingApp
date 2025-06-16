// Home.test.js
import React from 'react';
import { render, fireEvent, screen } from '@testing-library/react';
import Home from './home';
import { MemoryRouter } from 'react-router-dom';

// Mock useNavigate once at the top to avoid redefinition errors
jest.mock('react-router-dom', () => {
  const originalModule = jest.requireActual('react-router-dom');
  return {
    __esModule: true,
    ...originalModule,
    useNavigate: () => jest.fn(),
  };
});

describe('Home Component Tests', () => {
  beforeEach(() => {
    localStorage.clear();
  });

  test('renders search input and search button', () => {
    render(
      <MemoryRouter>
        <Home />
      </MemoryRouter>
    );

    const searchInput = screen.getByPlaceholderText(/Please select a search mode to search/i);
    expect(searchInput).toBeInTheDocument();

    // Use getByRole to ensure that only the button is selected
    const searchButton = screen.getByRole('button', { name: /Search/i });
    expect(searchButton).toBeInTheDocument();
  });

  test('triggers search when Enter key is pressed', () => {
    render(
      <MemoryRouter>
        <Home />
      </MemoryRouter>
    );

    const searchInput = screen.getByPlaceholderText(/Please select a search mode to search/i);
    fireEvent.change(searchInput, { target: { value: 'test query' } });
    fireEvent.keyDown(searchInput, { key: 'Enter', code: 'Enter' });

    expect(searchInput.value).toBe('test query');
  });

  test('triggers search when search button is clicked', () => {
    render(
      <MemoryRouter>
        <Home />
      </MemoryRouter>
    );

    const searchInput = screen.getByPlaceholderText(/Please select a search mode to search/i);
    fireEvent.change(searchInput, { target: { value: 'another test' } });

    const searchButton = screen.getByRole('button', { name: /Search/i });
    fireEvent.click(searchButton);

    expect(searchInput.value).toBe('another test');
  });
});

