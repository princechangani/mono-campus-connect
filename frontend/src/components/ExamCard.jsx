import React from 'react';
import { examService } from '../services/examService';

const ExamCard = ({ exam }) => {
    const handleEnroll = async () => {
        try {
            await examService.enrollStudent(exam.id, localStorage.getItem('userId'));
            alert('Successfully enrolled in exam');
        } catch (error) {
            alert('Failed to enroll in exam');
        }
    };

    return (
        <div className="border rounded p-4 mb-4">
            <h3 className="text-lg font-bold mb-2">{exam.title}</h3>
            <p className="text-gray-600 mb-2">Course: {exam.courseCode}</p>
            <p className="text-gray-600 mb-2">Type: {exam.type}</p>
            <div className="flex justify-between items-center">
                <div>
                    <p className="text-gray-600 mb-1">Start Date: {new Date(exam.startDate).toLocaleDateString()}</p>
                    <p className="text-gray-600">End Date: {new Date(exam.endDate).toLocaleDateString()}</p>
                </div>
                <button
                    onClick={handleEnroll}
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                >
                    Enroll
                </button>
            </div>
        </div>
    );
};

export default ExamCard;
