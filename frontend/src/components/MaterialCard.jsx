import React from 'react';
import { materialService } from '../services/materialService';

const MaterialCard = ({ material }) => {
    const handleDownload = () => {
        // Implement download logic
        console.log('Downloading material:', material.id);
    };

    return (
        <div className="border rounded p-4 mb-4">
            <h3 className="text-lg font-bold mb-2">{material.title}</h3>
            <p className="text-gray-600 mb-2">Course: {material.courseCode}</p>
            <p className="text-gray-600 mb-2">Type: {material.type}</p>
            <p className="text-gray-600 mb-4">Uploaded by: {material.uploadedBy}</p>
            <div className="flex justify-between items-center">
                <button
                    onClick={handleDownload}
                    className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
                >
                    Download
                </button>
            </div>
        </div>
    );
};

export default MaterialCard;
