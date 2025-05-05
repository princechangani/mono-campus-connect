import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth';

export const authService = {
    login: async (credentials) => {
        try {
            const request = {
                email: credentials.email,
                password: credentials.password
            };
            const response = await axios.post(`${API_URL}/login`, request, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            localStorage.setItem('token', response.data.token);
            return response.data;
        } catch (error) {
            if (error.response) {
                throw error.response.data;
            }
            throw { message: 'Login failed. Please try again.' };
        }
    },
    register: async (userData) => {
        try {
            const request = {
                email: userData.email,
                password: userData.password,
                firstName: userData.firstName,
                lastName: userData.lastName,
                role: userData.role
            };
            const response = await axios.post(`${API_URL}/register`, request, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            return response.data;
        } catch (error) {
            if (error.response) {
                throw error.response.data;
            }
            throw { message: 'Registration failed. Please try again.' };
        }
    }
};
