import React, { useState } from 'react';
import { profileService } from '../services/profileService';

const ProfileCard = ({ user }) => {
    const [isEditing, setIsEditing] = useState(false);
    const [formData, setFormData] = useState({
        firstName: user.firstName,
        lastName: user.lastName,
        email: user.email,
        phone: user.phone,
        address: user.address
    });

    const handleEdit = () => {
        setIsEditing(true);
    };

    const handleSave = async () => {
        try {
            await profileService.updateProfile(user.id, formData);
            setIsEditing(false);
            alert('Profile updated successfully');
        } catch (error) {
            alert('Failed to update profile');
        }
    };

    const handleCancel = () => {
        setFormData({
            firstName: user.firstName,
            lastName: user.lastName,
            email: user.email,
            phone: user.phone,
            address: user.address
        });
        setIsEditing(false);
    };

    return (
        <div className="bg-white rounded-lg shadow-md p-6">
            <div className="flex items-center mb-6">
                <div className="w-32 h-32 rounded-full overflow-hidden bg-gray-200 mr-6">
                    {user.profilePicture ? (
                        <img src={user.profilePicture} alt="Profile" className="w-full h-full object-cover" />
                    ) : (
                        <div className="flex items-center justify-center h-full">
                            <span className="text-2xl">{user.firstName[0]}{user.lastName[0]}</span>
                        </div>
                    )}
                </div>
                <div>
                    <h2 className="text-2xl font-bold mb-1">{user.firstName} {user.lastName}</h2>
                    <p className="text-gray-600">{user.role}</p>
                </div>
            </div>

            <div className="space-y-4">
                <div className="flex justify-between items-center">
                    <label className="font-medium">Email</label>
                    {isEditing ? (
                        <input
                            type="email"
                            value={formData.email}
                            onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                            className="border rounded p-2 flex-1"
                        />
                    ) : (
                        <span>{user.email}</span>
                    )}
                </div>

                <div className="flex justify-between items-center">
                    <label className="font-medium">Phone</label>
                    {isEditing ? (
                        <input
                            type="tel"
                            value={formData.phone}
                            onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                            className="border rounded p-2 flex-1"
                        />
                    ) : (
                        <span>{user.phone}</span>
                    )}
                </div>

                <div className="flex justify-between items-center">
                    <label className="font-medium">Address</label>
                    {isEditing ? (
                        <textarea
                            value={formData.address}
                            onChange={(e) => setFormData({ ...formData, address: e.target.value })}
                            className="border rounded p-2 flex-1"
                        />
                    ) : (
                        <span>{user.address}</span>
                    )}
                </div>

                {isEditing ? (
                    <div className="flex justify-end space-x-4">
                        <button
                            onClick={handleCancel}
                            className="px-4 py-2 text-gray-600 hover:text-gray-800"
                        >
                            Cancel
                        </button>
                        <button
                            onClick={handleSave}
                            className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                        >
                            Save
                        </button>
                    </div>
                ) : (
                    <button
                        onClick={handleEdit}
                        className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
                    >
                        Edit Profile
                    </button>
                )}
            </div>
        </div>
    );
};

export default ProfileCard;
