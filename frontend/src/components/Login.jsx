import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authService } from '../services/authService';
import './Login.css';

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const request = {
                email,
                password
            };
            const response = await authService.login(request);
            localStorage.setItem('user', JSON.stringify(response.user));
            navigate('/');
        } catch (err) {
            setError(err.response?.data?.message || 'Login failed. Please try again.');
        }
    };

    return (
        <div className="login-container">
            <div className="login-card">
                <div className="login-content">
                    <div className="text-center">
                        <h2 className="login-title">
                            Welcome Back
                        </h2>
                        <p className="login-subtitle">Sign in to your account</p>
                    </div>

                    <form className="space-y-6" onSubmit={handleSubmit}>
                        {error && (
                            <div className="error-message">
                                <div className="text-sm">{error}</div>
                            </div>
                        )}

                        <div className="form-group">
                            <label htmlFor="email" className="form-label">
                                Email address
                            </label>
                            <input
                                id="email"
                                name="email"
                                type="email"
                                required
                                className="form-input"
                                placeholder="Enter your email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />
                        </div>

                        <div className="form-group">
                            <label htmlFor="password" className="form-label">
                                Password
                            </label>
                            <input
                                id="password"
                                name="password"
                                type="password"
                                required
                                className="form-input"
                                placeholder="Enter your password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />
                        </div>

                        <div className="flex items-center justify-between">
                            <div className="flex items-center">
                                <input
                                    id="remember-me"
                                    name="remember-me"
                                    type="checkbox"
                                    className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                                />
                                <label htmlFor="remember-me" className="ml-2 block text-sm text-gray-900">
                                    Remember me
                                </label>
                            </div>
                            <div className="text-sm">
                                <a href="#" className="font-medium text-blue-600 hover:text-blue-500">
                                    Forgot your password?
                                </a>
                            </div>
                        </div>

                        <div>
                            <button
                                type="submit"
                                className="login-button"
                                disabled={!email || !password}
                            >
                                Sign in
                            </button>
                        </div>
                    </form>

                    <div className="social-login">
                        <div className="social-divider">
                            <span className="social-text">Or continue with</span>
                        </div>

                        <div className="social-buttons">
                            <button
                                type="button"
                                className="social-button"
                            >
                                <svg
                                    className="social-icon"
                                    aria-hidden="true"
                                    fill="currentColor"
                                    viewBox="0 0 20 20"
                                >
                                    <path d="M6.29 18.251c7.547 0 11.675-6.253 11.675-11.675 0-.178 0-.355-.012-.53A8.348 8.348 0 0020 8.34a8.34 8.34 0 01-2.357.184 4.482 4.482 0 003.541-3.898 9.58 9.58 0 01-3.159-1.899 4.389 4.389 0 00-.351 1.054 4.404 4.404 0 01-1.503-.203 4.385 4.385 0 001.029 3.187 9.521 9.521 0 01-2.046 1.1 4.422 4.422 0 010 6.35c0 1.03.098 2.054.294 3.07-.544-.093-1.084-.205-1.611-.323a4.416 4.416 0 00.005 1.428 4.428 4.428 0 01-.7 1.834 9.587 9.587 0 01-1.989.887 4.389 4.389 0 01-.398.085 4.385 4.385 0 00-.596 1.99a4.404 4.404 0 01-1.557-.235v.002a4.421 4.421 0 003.82 3.82c-.964 1.076-2.034 1.923-3.32 2.517.987.32 2.049.537 3.178.693a4.42 4.42 0 011.898 0c1.129-.156 2.191-.373 3.179-.694zm-5.492-5.95a4.426 4.426 0 00-.923-1.787M10 9a4.49 4.49 0 00-4.494 4.493 4.49 4.49 0 01-2.243-.716 4.49 4.49 0 004.485-4.489 4.493 4.493 0 011.507-.027A4.485 4.485 0 0010 9z" />
                                </svg>
                            </button>
                            <button
                                type="button"
                                className="social-button"
                            >
                                <svg
                                    className="social-icon"
                                    aria-hidden="true"
                                    fill="currentColor"
                                    viewBox="0 0 20 20"
                                >
                                    <path fillRule="evenodd" d="M20 10c0-5.523-4.477-10-10-10S0 4.477 0 10c0 4.991 3.657 9.128 8.438 9.878v-6.987h-2.54V10h2.54V7.797c0-2.506 1.492-3.89 3.777-3.89 1.094 0 2.238.195 2.238.195v2.46h-1.26c-1.243 0-1.63.771-1.63 1.562V10h2.773l-.443 2.89h-2.33v6.988C16.343 19.128 20 14.991 20 10z" clipRule="evenodd" />
                                </svg>
                            </button>
                            <button
                                type="button"
                                className="social-button"
                            >
                                <svg
                                    className="social-icon"
                                    aria-hidden="true"
                                    fill="currentColor"
                                    viewBox="0 0 20 20"
                                >
                                    <path d="M6.29 18.251c7.547 0 11.675-6.253 11.675-11.675 0-.178 0-.355-.012-.53A8.348 8.348 0 0020 8.34a8.34 8.34 0 01-2.357.184 4.482 4.482 0 003.541-3.898 9.58 9.58 0 01-3.159-1.899 4.389 4.389 0 00-.351 1.054 4.404 4.404 0 01-1.503-.203 4.385 4.385 0 001.029 3.187 9.521 9.521 0 01-2.046 1.1 4.422 4.422 0 010 6.35c0 1.03.098 2.054.294 3.07-.544-.093-1.084-.205-1.611-.323a4.416 4.416 0 00.005 1.428 4.428 4.428 0 01-.7 1.834 9.587 9.587 0 01-1.989.887 4.389 4.389 0 01-.398.085 4.385 4.385 0 00-.596 1.99a4.404 4.404 0 01-1.557-.235v.002a4.421 4.421 0 003.82 3.82c-.964 1.076-2.034 1.923-3.32 2.517.987.32 2.049.537 3.178.693a4.42 4.42 0 011.898 0c1.129-.156 2.191-.373 3.179-.694zm-5.492-5.95a4.426 4.426 0 00-.923-1.787M10 9a4.49 4.49 0 00-4.494 4.493 4.49 4.49 0 01-2.243-.716 4.49 4.49 0 004.485-4.489 4.493 4.493 0 011.507-.027A4.485 4.485 0 0010 9z" />
                                </svg>
                            </button>
                        </div>
                    </div>

                    <div className="register-link">
                        <p className="text-sm text-gray-600">
                            Don't have an account?{' '}
                            <a href="/register" className="font-medium text-blue-600 hover:text-blue-500">
                                Sign up
                            </a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Login;
