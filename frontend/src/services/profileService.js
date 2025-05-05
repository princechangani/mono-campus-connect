import axios from 'axios';

const API_URL = 'http://localhost:8080/api/profile';

export const profileService = {
    getProfile: async (userId) => {
        try {
            const response = await axios.post(`${API_URL}`, { userId });
            return response.data;
        } catch (error) {
            throw new Error('Failed to fetch profile');
        }
    },

    updateProfile: async (userId, formData, profilePicture) => {
        try {
            const data = new FormData();
            if (profilePicture) {
                data.append('profilePicture', profilePicture);
            }
            Object.entries(formData).forEach(([key, value]) => {
                if (value !== null && value !== undefined) {
                    data.append(key, value);
                }
            });

            const response = await axios.put(`${API_URL}/${userId}/profile`, data, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
            return response.data;
        } catch (error) {
            throw new Error('Failed to update profile');
        }
    },

    deleteProfilePicture: async (userId) => {
        try {
            await axios.delete(`${API_URL}/${userId}/picture`);
        } catch (error) {
            throw new Error('Failed to delete profile picture');
        }
    },

    changePassword: async (userId, currentPassword, newPassword) => {
        try {
            await axios.post(`${API_URL}/${userId}/password`, {
                currentPassword,
                newPassword
            });
        } catch (error) {
            throw new Error('Failed to change password');
        }
    }
};
