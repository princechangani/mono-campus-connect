import React, { useState, useEffect } from 'react';
import { courseService } from '../services/courseService';
import { examService } from '../services/examService';
import { materialService } from '../services/materialService';
import { eventService } from '../services/eventService';
import { profileService } from '../services/profileService';
import { resultService } from '../services/resultService';
import CourseCard from './CourseCard';
import ExamCard from './ExamCard';
import MaterialCard from './MaterialCard';
import EventCard from './EventCard';
import ProfileCard from './ProfileCard';
import ResultCard from './ResultCard';
import { useAuth } from "../context/AuthContext";

const Dashboard = () => {
    const [courses, setCourses] = useState([]);
    const [exams, setExams] = useState([]);
    const [materials, setMaterials] = useState([]);
    const [events, setEvents] = useState([]);
    const [results, setResults] = useState([]);
    const [user, setUser] = useState(null);
    const { currentUser } = useAuth();

    useEffect(() => {
        const fetchAllData = async () => {
            try {
                const [coursesData, examsData, materialsData, eventsData, resultsData] = await Promise.all([
                    courseService.getAll(),
                    examService.getAll(),
                    materialService.getAll(),
                    eventService.getAll(),
                    resultService.getResultsByStudent(currentUser.id)
                ]);
                setCourses(coursesData);
                setExams(examsData);
                setMaterials(materialsData);
                setEvents(eventsData);
                setResults(resultsData);

                // Fetch user profile
                const profile = await profileService.getProfile(currentUser.id);
                setUser(profile);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchAllData();
    }, []);

    return (
        <div className="container mx-auto px-4 py-8">
            <h1 className="text-3xl font-bold mb-8">Campus Connect Dashboard</h1>

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 mb-8">
                <div className="bg-white rounded-lg shadow p-6">
                    <h2 className="text-xl font-semibold mb-4">Profile</h2>
                    <ProfileCard user={user} />
                </div>

                <div className="bg-white rounded-lg shadow p-6">
                    <h2 className="text-xl font-semibold mb-4">Recent Results</h2>
                    <div className="space-y-4">
                        {results.map((result) => (
                            <ResultCard key={result.id} result={result} />
                        ))}
                    </div>
                </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
                <div className="bg-white rounded-lg shadow p-6">
                    <h2 className="text-xl font-semibold mb-4">Courses</h2>
                    <div className="space-y-4">
                        {courses.map((course) => (
                            <CourseCard key={course.id} course={course} />
                        ))}
                    </div>
                </div>

                <div className="bg-white rounded-lg shadow p-6">
                    <h2 className="text-xl font-semibold mb-4">Upcoming Exams</h2>
                    <div className="space-y-4">
                        {exams.map((exam) => (
                            <ExamCard key={exam.id} exam={exam} />
                        ))}
                    </div>
                </div>

                <div className="bg-white rounded-lg shadow p-6">
                    <h2 className="text-xl font-semibold mb-4">Campus Events</h2>
                    <div className="space-y-4">
                        {events.map((event) => (
                            <EventCard key={event.id} event={event} />
                        ))}
                    </div>
                </div>

                <div className="bg-white rounded-lg shadow p-6">
                    <h2 className="text-xl font-semibold mb-4">Course Materials</h2>
                    <div className="space-y-4">
                        {materials.map((material) => (
                            <MaterialCard key={material.id} material={material} />
                        ))}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Dashboard;
