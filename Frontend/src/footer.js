import React from 'react';
import { Link } from 'react-router-dom';
import './footer.css'; // <-- Import the CSS

const Footer = () => (
  <footer className="app-footer">
    <p>
      &copy; {new Date().getFullYear()} Structural Soils Limited &nbsp;|&nbsp;
      <Link to="https://soils.co.uk/contact/" className="footer-link">
        Contact Us
      </Link>
    </p>
  </footer>
);

export default Footer;
