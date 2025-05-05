import axios from 'axios';

const API_URL = 'http://localhost:8080/api/results';

export const resultService = {
    getResultsByStudent: async (studentId) => {
        try {
            const response = await axios.get(`${API_URL}/student/${studentId}`);
            return response.data;
        } catch (error) {
            throw new Error('Failed to fetch results');
        }
    },

    getResultsByStatus: async (status) => {
        try {
            const response = await axios.get(`${API_URL}/status/${status}`);
            return response.data;
        } catch (error) {
            throw new Error('Failed to fetch results by status');
        }
    },

    updateResultStatus: async (id, status) => {
        try {
            await axios.put(`${API_URL}/${id}/status`, { status });
        } catch (error) {
            throw new Error('Failed to update result status');
        }
    }
};
