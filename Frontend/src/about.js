import React from 'react';
import './App.css'; 

const About = () => {
    return (
        <div className="about-section">
            {/* Add a new container with a white background */}
            <div className="about-content-wrapper">
                <h1 className="about-title">GeoTest Finder</h1>
                <p className="about-description">
                    GeoTest Finder is an application designed to streamline the search and management of geological test information. The main features include:
                </p>
                <ul className="about-list">
                    <li><strong>Paginated Test List:</strong> A scrollable list of tests that can be sorted by ID, name, parameter, or method, helping users locate specific tests quickly.</li>
                    <li><strong>Detailed Test View:</strong> Each test item opens a floating, resizable, and draggable detail window, offering in-depth information on each test.</li>
                    <li><strong>Search and History Management:</strong> A customizable search bar with options to search by test name, parameter, or method. The app saves and displays past search queries for easy access, with options to remove specific history items.</li>
                    <li><strong>Responsive and Dynamic UI:</strong> The interface includes interactive components like buttons, history dropdowns, and real-time updates, making navigation intuitive.</li>
                </ul>
            </div>
        </div>
    );
};

export default About;
