import React, { createContext, useState, useContext } from 'react';

// Create the context
const SelectedTestsContext = createContext();

// Create a provider component
export const SelectedTestsProvider = ({ children }) => {
    const [selectedTests, setSelectedTests] = useState([]);

    // Function to toggle test selection
    const handleToggleTest = (id) => {
        if (id == null){
            
        }
        setSelectedTests((prev) => {
            const updatedSelection = prev.includes(id)
                ? prev.filter((tid) => tid !== id)
                : [...prev, id];
            return updatedSelection;
        });
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
