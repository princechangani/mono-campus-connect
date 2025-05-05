import axios from 'axios';

const API_URL = 'http://localhost:8080/api/courses';

export const courseService = {
    getAll: async () => {
        try {
            const response = await axios.get(API_URL);
            return response.data;
        } catch (error) {
            throw new Error('Failed to fetch courses');
        }
    },

    getByDepartment: async (department) => {
        try {
            const response = await axios.get(`${API_URL}/department/${department}`);
            return response.data;
        } catch (error) {
            throw new Error('Failed to fetch courses by department');
        }
    },

    getBySemester: async (semester) => {
        try {
            const response = await axios.get(`${API_URL}/semester/${semester}`);
            return response.data;
        } catch (error) {
            throw new Error('Failed to fetch courses by semester');
        }
    },

    getCourse: async (courseCode) => {
        try {
            const response = await axios.get(`${API_URL}/${courseCode}`);
            return response.data;
        } catch (error) {
            throw new Error('Failed to fetch course');
        }
    },

    create: async (course) => {
        try {
            const response = await axios.post(API_URL, course);
            return response.data;
        } catch (error) {
            throw new Error('Failed to create course');
        }
    },

    update: async (id, course) => {
        try {
            const response = await axios.put(`${API_URL}/${id}`, course);
            return response.data;
        } catch (error) {
            throw new Error('Failed to update course');
        }
    },

    delete: async (id) => {
        try {
            await axios.delete(`${API_URL}/${id}`);
        } catch (error) {
            throw new Error('Failed to delete course');
        }
    }
};
