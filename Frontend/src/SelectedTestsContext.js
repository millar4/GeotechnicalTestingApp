import React, { createContext, useState, useContext } from 'react';

// Create the context
const SelectedTestsContext = createContext();

// Create a provider component
export const SelectedTestsProvider = ({ children }) => {
    const [selectedTests, setSelectedTests] = useState([]);

    // Function to toggle test selection
    const handleToggleTest = (testId) => {
    // Toggle the test selection in the selectedTests array
    if (selectedTests.includes(testId)) {
        // If test is already selected, remove it from the array
        setSelectedTests(selectedTests.filter(id => id !== testId));
    } else {
        // Otherwise, add it to the array
        setSelectedTests([...selectedTests, testId]);
    }
};

    return (
        <SelectedTestsContext.Provider value={{ selectedTests, handleToggleTest }}>
            {children}
        </SelectedTestsContext.Provider>
    );
};

// Create a custom hook to use the context
export const useSelectedTests = () => {
    return useContext(SelectedTestsContext);
};
