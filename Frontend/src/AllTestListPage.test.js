// AllTestListPage.test.js
import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import AllTestListPage from './AllTestListPage';
import { BrowserRouter } from 'react-router-dom';

// Mock window.scrollTo to avoid "Not implemented" errors in JSDOM
beforeAll(() => {
  Object.defineProperty(window, 'scrollTo', {
    writable: true,
    value: jest.fn(),
  });
});

// Mock fetch, providing response.status to prevent errors
beforeAll(() => {
  jest.spyOn(global, 'fetch').mockImplementation((url) => {
    if (url?.includes('/groups')) {
      return Promise.resolve({
        ok: true,
        status: 200,
        json: async () => ['GroupA', 'GroupB'],
      });
    }
    return Promise.resolve({
      ok: true,
      status: 200,
      json: async () => [],
    });
  });
});

afterAll(() => {
  global.fetch.mockRestore();
  delete window.scrollTo;
});

function renderAllTestListPage(routeState = {}) {
  return render(
    <BrowserRouter>
      <AllTestListPage location={{ state: routeState }} />
    </BrowserRouter>
  );
}

describe('AllTestListPage Tests', () => {
  beforeEach(() => {
    localStorage.setItem('token', 'fake_token');
    localStorage.setItem('isLoggedIn', 'true');
  });

  afterEach(() => {
    localStorage.clear();
    jest.clearAllMocks();
  });

  test('01: Database switch buttons - Soil/Rock', async () => {
    renderAllTestListPage();
    await waitFor(() => {
      const soilBtn = screen.getByRole('button', { name: /soil/i });
      const rockBtn = screen.getByRole('button', { name: /rock/i });
      fireEvent.click(rockBtn);
      fireEvent.click(soilBtn);
    });
  });

  test('02: Sorting buttons (Id/Name/Group/Method/Parameter)', async () => {
    renderAllTestListPage();
    await waitFor(() => {
      const idBtn = screen.getByRole('button', { name: /id arrangement/i });
      const nameBtn = screen.getByRole('button', { name: /name arrangement/i });
      const groupBtn = screen.getByRole('button', { name: /group arrangement/i });
      const methodBtn = screen.getByRole('button', { name: /method arrangement/i });
      const paramBtn = screen.getByRole('button', { name: /parameter arrangement/i });
      fireEvent.click(nameBtn);
      fireEvent.click(groupBtn);
      fireEvent.click(methodBtn);
      fireEvent.click(paramBtn);
    });
  });

  test('03: Group dropdown selection', async () => {
    renderAllTestListPage();
    await waitFor(() => {
      const selects = screen.getAllByRole('combobox');
      fireEvent.change(selects[0], { target: { value: 'GroupB' } });
    });
  });

  test('04: Items Per Page dropdown (e.g. select 50)', async () => {
    renderAllTestListPage();
    await waitFor(() => {
      const selects = screen.getAllByRole('combobox');
      if (selects[1]) {
        fireEvent.change(selects[1], { target: { value: '50' } });
      }
    });
  });

  test('05: Logged in => shows "Testlist" & "Add Item"', async () => {
    renderAllTestListPage();
    await waitFor(() => {
      expect(screen.getByText(/testlist/i)).toBeInTheDocument();
      expect(screen.getByRole('button', { name: /add item/i })).toBeInTheDocument();
    });
  });

  test('06: Logged out => shows a logout prompt', async () => {
    localStorage.removeItem('token');
    localStorage.removeItem('isLoggedIn');
    renderAllTestListPage();
    await waitFor(() => {
      expect(screen.getByText(/you are currently logged out/i)).toBeInTheDocument();
    });
  });

  test('07: Animation control button => Stop/Start Animation', async () => {
    renderAllTestListPage();
    await waitFor(() => {
      const animBtn = screen.getByRole('button', { name: /stop animation/i });
      fireEvent.click(animBtn);
    });
  });

  test('08: Pagination => Previous/Next/Jump', async () => {
    renderAllTestListPage();
    await waitFor(() => {
      const prevBtn = screen.getByRole('button', { name: /previous/i });
      const nextBtn = screen.getByRole('button', { name: /next/i });
      const jumpBtn = screen.getByRole('button', { name: /jump/i });
      fireEvent.click(nextBtn);
      fireEvent.click(prevBtn);
      const jumpInput = screen.getByPlaceholderText(/page #/i);
      fireEvent.change(jumpInput, { target: { value: '3' } });
      fireEvent.click(jumpBtn);
    });
  });
});
