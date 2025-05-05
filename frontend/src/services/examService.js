import axios from 'axios';

const API_URL = 'http://localhost:8080/api/exams';

export const examService = {
    getAll: async () => {
        try {
            const response = await axios.get(API_URL);
            return response.data;
        } catch (error) {
            throw new Error('Failed to fetch exams');
        }
    },

    create: async (examData) => {
        try {
            const formData = new FormData();
            Object.entries(examData).forEach(([key, value]) => {
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
            throw new Error('Failed to create exam');
        }
    },

    getBetweenDates: async (startDate, endDate) => {
        try {
            const response = await axios.get(`${API_URL}/between-dates`, {
                params: {
                    startDate,
                    endDate
                }
            });
            return response.data;
        } catch (error) {
            throw new Error('Failed to fetch exams between dates');
        }
    },

    enrollStudent: async (examId, studentId) => {
        try {
            const response = await axios.post(`${API_URL}/enroll`, {
                examId,
                studentId
            });
            return response.data;
        } catch (error) {
            throw new Error('Failed to enroll student');
        }
    },

    unenrollStudent: async (examId, studentId) => {
        try {
            await axios.post(`${API_URL}/unenroll`, {
                examId,
                studentId
            });
        } catch (error) {
            throw new Error('Failed to unenroll student');
        }
    }
};
