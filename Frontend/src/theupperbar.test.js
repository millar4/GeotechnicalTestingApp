// theupperbar.test.js
import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import Theupperbar from './theupperbar';
import { BrowserRouter } from 'react-router-dom';

beforeEach(() => {
  localStorage.clear();
});

describe('Theupperbar component', () => {
  test('Shows "Login" when not logged in', () => {
    render(
      <BrowserRouter>
        <Theupperbar
          searchcontent=""
          updatecontent={() => {}}
          searchfunction={() => {}}
          searchhistory={[]}
          showhistory={false}
          handleFocus={() => {}}
          handleBlur={() => {}}
          deletehistoryitem={() => {}}
          handleClickHistory={() => {}}
          pattern="Quick Search"
          updatepattern={() => {}}
          databaseType="soil"
          setDatabaseType={() => {}}
        />
      </BrowserRouter>
    );
    const loginButton = screen.getByRole('button', { name: /login/i });
    expect(loginButton).toBeInTheDocument();
  });

  test('Clicking "Login" opens login modal', () => {
    render(
      <BrowserRouter>
        <Theupperbar
          searchcontent=""
          updatecontent={() => {}}
          searchfunction={() => {}}
          searchhistory={[]}
          showhistory={false}
          handleFocus={() => {}}
          handleBlur={() => {}}
          deletehistoryitem={() => {}}
          handleClickHistory={() => {}}
          pattern="Quick Search"
          updatepattern={() => {}}
          databaseType="soil"
          setDatabaseType={() => {}}
        />
      </BrowserRouter>
    );
    const loginButton = screen.getByRole('button', { name: /login/i });
    fireEvent.click(loginButton);
    const modalTitle = screen.getByRole('heading', { name: /login/i });
    expect(modalTitle).toBeInTheDocument();
    const usernameInput = screen.getByPlaceholderText(/username/i);
    expect(usernameInput).toBeInTheDocument();
  });

  test('Displays "Logout" if token is found', () => {
    localStorage.setItem('token', 'fake_jwt_token');
    render(
      <BrowserRouter>
        <Theupperbar
          searchcontent=""
          updatecontent={() => {}}
          searchfunction={() => {}}
          searchhistory={[]}
          showhistory={false}
          handleFocus={() => {}}
          handleBlur={() => {}}
          deletehistoryitem={() => {}}
          handleClickHistory={() => {}}
          pattern="Quick Search"
          updatepattern={() => {}}
          databaseType="soil"
          setDatabaseType={() => {}}
        />
      </BrowserRouter>
    );
    const logoutButton = screen.getByRole('button', { name: /logout/i });
    expect(logoutButton).toBeInTheDocument();
  });

  test('Contains Home, Test List, and About links', () => {
    render(
      <BrowserRouter>
        <Theupperbar
          searchcontent=""
          updatecontent={() => {}}
          searchfunction={() => {}}
          searchhistory={[]}
          showhistory={false}
          handleFocus={() => {}}
          handleBlur={() => {}}
          deletehistoryitem={() => {}}
          handleClickHistory={() => {}}
          pattern="Quick Search"
          updatepattern={() => {}}
          databaseType="soil"
          setDatabaseType={() => {}}
        />
      </BrowserRouter>
    );
    const homeLink = screen.getByRole('link', { name: /home/i });
    const testListLink = screen.getByRole('link', { name: /test list/i });
    const aboutLink = screen.getByRole('link', { name: /about/i });
    expect(homeLink).toBeInTheDocument();
    expect(testListLink).toBeInTheDocument();
    expect(aboutLink).toBeInTheDocument();
  });
});
