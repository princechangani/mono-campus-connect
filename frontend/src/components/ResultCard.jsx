import React from 'react';
import { Link } from 'react-router-dom';

const ResultCard = ({ result }) => {
    const getGradeColor = (grade) => {
        switch (grade) {
            case 'A':
            case 'A+':
                return 'bg-green-100 text-green-800';
            case 'B':
            case 'B+':
                return 'bg-blue-100 text-blue-800';
            case 'C':
            case 'C+':
                return 'bg-yellow-100 text-yellow-800';
            default:
                return 'bg-gray-100 text-gray-800';
        }
    };

    return (
        <div className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
            <div className="flex justify-between items-center mb-4">
                <h3 className="text-xl font-semibold">{result.exam.title}</h3>
                <span className="text-sm text-gray-500">
                    {new Date(result.exam.startDate).toLocaleDateString()} -
                    {new Date(result.exam.endDate).toLocaleDateString()}
                </span>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                    <p className="text-gray-600 mb-1">Student ID:</p>
                    <p className="font-medium">{result.studentId}</p>
                </div>
                <div>
                    <p className="text-gray-600 mb-1">Marks Obtained:</p>
                    <p className="font-medium">{result.obtainedMarks}</p>
                </div>
                <div>
                    <p className="text-gray-600 mb-1">Total Marks:</p>
                    <p className="font-medium">{result.totalMarks}</p>
                </div>
                <div>
                    <p className="text-gray-600 mb-1">Grade:</p>
                    <div className={`px-3 py-1 rounded-full text-sm ${getGradeColor(result.grade)}`}>
                        {result.grade}
                    </div>
                </div>
            </div>

            {result.comments && (
                <div className="mt-4">
                    <p className="text-gray-600 mb-1">Comments:</p>
                    <p className="text-gray-700">{result.comments}</p>
                </div>
            )}

            <div className="mt-4">
                <Link
                    to={`/exams/${result.exam.id}`}
                    className="text-blue-500 hover:text-blue-700"
                >
                    View Exam Details
                </Link>
            </div>
        </div>
    );
};

export default ResultCard;
