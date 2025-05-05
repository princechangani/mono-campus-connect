import React from 'react';
import { Link } from 'react-router-dom';

const EventCard = ({ event }) => {
    return (
        <div className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow">
            <div className="relative w-full h-48 mb-4">
                <img
                    src={event.image}
                    alt={event.title}
                    className="w-full h-full object-cover rounded-lg"
                />
                <div className="absolute top-4 right-4 bg-gray-900 text-white px-3 py-1 rounded text-sm">
                    {new Date(event.createdAt).toLocaleDateString()}
                </div>
            </div>
            <h3 className="text-xl font-semibold mb-2">{event.title}</h3>
            <p className="text-gray-600 mb-4 line-clamp-3">{event.description}</p>
            <div className="flex justify-between items-center">
                <p className="text-sm text-gray-500">Posted by: {event.postedBy}</p>
                <Link
                    to={`/events/${event.id}`}
                    className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                >
                    View Details
                </Link>
            </div>
        </div>
    );
};

export default EventCard;
