import React from 'react';
import { Link } from 'react-router-dom';
import './footer.css'; // <-- Import the CSS

const Footer = () => (
  <footer className="app-footer">
    <p>
      &copy; {new Date().getFullYear()} Structural Soils Limited &nbsp;|&nbsp;
      <a href="https://soils.co.uk/contact/" className="footer-link">
        Find Us
      </a>
      &nbsp;|&nbsp;
      <a href="mailto:asksoils@soils.co.uk" className="footer-link">
        Email Us
      </a>
    </p>
  </footer>
);

export default Footer;
