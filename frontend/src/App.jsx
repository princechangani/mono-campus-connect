import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { AuthProvider, useAuth } from './context/AuthContext';
import { RouteProvider } from './context/RouteContext';
import Login from './components/Login.jsx';
import Register from './components/Register.jsx';
import Dashboard from './components/Dashboard';
import Navigation from './components/Navigation';

const theme = createTheme({
    palette: {
        primary: {
            main: '#1976d2',
        },
        secondary: {
            main: '#dc004e',
        },
    },
});

const AuthRoute = ({ children }) => {
    const { currentUser } = useAuth();
    return currentUser ? children : <Navigate to="/login" replace />;
};

const PublicRoute = ({ children }) => {
    const { currentUser } = useAuth();
    return currentUser ? <Navigate to="/" replace /> : children;
};

function App() {
    return (
        <AuthProvider>
            <ThemeProvider theme={theme}>
                <CssBaseline />
                <Router>
                    <RouteProvider>
                        <Routes>
                            {/* Public Routes */}
                            <Route path="/login" element={<PublicRoute><Login /></PublicRoute>} />
                            <Route path="/register" element={<PublicRoute><Register /></PublicRoute>} />

                            {/* Protected Routes */}
                            <Route
                                path="/"
                                element={
                                    <AuthRoute>
                                        <div className="min-h-screen bg-gray-100">
                                            <Navigation />
                                            <Dashboard />
                                        </div>
                                    </AuthRoute>
                                }
                            />
                            <Route
                                path="/courses"
                                element={
                                    <AuthRoute>
                                        <div className="min-h-screen bg-gray-100">
                                            <Navigation />
                                            <Dashboard />
                                        </div>
                                    </AuthRoute>
                                }
                            />
                            <Route
                                path="/exams"
                                element={
                                    <AuthRoute>
                                        <div className="min-h-screen bg-gray-100">
                                            <Navigation />
                                            <Dashboard />
                                        </div>
                                    </AuthRoute>
                                }
                            />
                            <Route
                                path="/materials"
                                element={
                                    <AuthRoute>
                                        <div className="min-h-screen bg-gray-100">
                                            <Navigation />
                                            <Dashboard />
                                        </div>
                                    </AuthRoute>
                                }
                            />
                            <Route
                                path="/events"
                                element={
                                    <AuthRoute>
                                        <div className="min-h-screen bg-gray-100">
                                            <Navigation />
                                            <Dashboard />
                                        </div>
                                    </AuthRoute>
                                }
                            />
                            <Route
                                path="/results"
                                element={
                                    <AuthRoute>
                                        <div className="min-h-screen bg-gray-100">
                                            <Navigation />
                                            <Dashboard />
                                        </div>
                                    </AuthRoute>
                                }
                            />
                            <Route
                                path="/profile"
                                element={
                                    <AuthRoute>
                                        <div className="min-h-screen bg-gray-100">
                                            <Navigation />
                                            <Dashboard />
                                        </div>
                                    </AuthRoute>
                                }
                            />
                        </Routes>
                    </RouteProvider>
                </Router>
            </ThemeProvider>
        </AuthProvider>
    );
}

export default App;
