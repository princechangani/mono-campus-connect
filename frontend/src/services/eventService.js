import axios from 'axios';

const API_URL = 'http://localhost:8080/api/events';

export const eventService = {
    getAll: async () => {
        try {
            const response = await axios.get(API_URL);
            return response.data;
        } catch (error) {
            throw new Error('Failed to fetch events');
        }
    },

    create: async (eventData) => {
        try {
            const formData = new FormData();
            Object.entries(eventData).forEach(([key, value]) => {
                if (value !== null && value !== undefined) {
                    formData.append(key, value);
                }
            });
            
            const response = await axios.post(API_URL, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
            return response.data;
        } catch (error) {
            throw new Error('Failed to create event');
        }
    },

    getEvent: async (id) => {
        try {
            const response = await axios.get(`${API_URL}/${id}`);
            return response.data;
        } catch (error) {
            throw new Error('Failed to fetch event');
        }
    }
};
