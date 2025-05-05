import React, { createContext, useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const RouteContext = createContext();

export const RouteProvider = ({ children }) => {
    const navigate = useNavigate();
    const [currentRoute, setCurrentRoute] = useState('/');

    const handleRouteChange = (path) => {
        setCurrentRoute(path);
        navigate(path);
    };

    return (
        <RouteContext.Provider value={{ currentRoute, handleRouteChange }}>
            {children}
        </RouteContext.Provider>
    );
};

export const useRoute = () => {
    const context = useContext(RouteContext);
    if (!context) {
        throw new Error('useRoute must be used within a RouteProvider');
    }
    return context;
};
