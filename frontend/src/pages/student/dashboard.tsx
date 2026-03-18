import React from 'react';
import { Layout } from '@/components/layout/Layout';
import { Card, Button } from '@/components/ui/Common';
import { BookOpen, FileText, Clock, CheckCircle, AlertCircle } from 'lucide-react';

const StudentDashboard = () => {
  const navItems = [
    { label: 'Dashboard', href: '/student/dashboard' },
    { label: 'Courses', href: '/student/courses' },
    { label: 'Attendance', href: '/student/attendance' },
    { label: 'Results', href: '/student/results' },
    { label: 'Exams', href: '/student/exams' },
    { label: 'Materials', href: '/student/materials' },
  ];

  const courses = [
    { id: 1, name: 'Data Structures', instructor: 'Dr. Smith', status: 'Active' },
    { id: 2, name: 'Database Systems', instructor: 'Dr. Johnson', status: 'Active' },
    { id: 3, name: 'Web Development', instructor: 'Ms. Brown', status: 'Active' },
  ];

  const announcements = [
    { id: 1, title: 'Mid-term Exams Schedule', date: '2026-04-15', priority: 'high' },
    { id: 2, title: 'Class Cancelled on Friday', date: '2026-03-20', priority: 'normal' },
    { id: 3, title: 'Assignment Submission Extended', date: '2026-03-19', priority: 'normal' },
  ];

  return (
    <Layout navItems={navItems} title="Student Dashboard">
      {/* Welcome Section */}
      <div className="mb-8">
        <h2 className="text-2xl font-bold text-gray-900 mb-2">Welcome Back, Student!</h2>
        <p className="text-gray-600">Here's your academic overview for this semester</p>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        <Card>
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-600 text-sm">Enrolled Courses</p>
              <p className="text-3xl font-bold mt-2">3</p>
            </div>
            <BookOpen className="w-8 h-8 text-blue-500" />
          </div>
        </Card>

        <Card>
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-600 text-sm">Attendance</p>
              <p className="text-3xl font-bold mt-2">85%</p>
            </div>
            <CheckCircle className="w-8 h-8 text-green-500" />
          </div>
        </Card>

        <Card>
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-600 text-sm">GPA</p>
              <p className="text-3xl font-bold mt-2">3.8</p>
            </div>
            <FileText className="w-8 h-8 text-purple-500" />
          </div>
        </Card>

        <Card>
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-600 text-sm">Semester</p>
              <p className="text-3xl font-bold mt-2">4</p>
            </div>
            <Clock className="w-8 h-8 text-orange-500" />
          </div>
        </Card>
      </div>

      {/* Current Courses */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-8">
        <div className="lg:col-span-2">
          <Card title="Current Courses">
            <div className="space-y-3">
              {courses.map((course) => (
                <div key={course.id} className="p-4 bg-gray-50 rounded-lg flex justify-between items-center">
                  <div>
                    <h4 className="font-medium text-gray-900">{course.name}</h4>
                    <p className="text-sm text-gray-600">Instructor: {course.instructor}</p>
                  </div>
                  <Button variant="secondary" size="sm">
                    View
                  </Button>
                </div>
              ))}
            </div>
          </Card>
        </div>

        {/* Announcements */}
        <Card title="Announcements">
          <div className="space-y-3">
            {announcements.map((announcement) => (
              <div
                key={announcement.id}
                className={`p-3 rounded-lg border-l-4 ${
                  announcement.priority === 'high'
                    ? 'border-red-500 bg-red-50'
                    : 'border-blue-500 bg-blue-50'
                }`}
              >
                <h4 className="font-medium text-sm text-gray-900">{announcement.title}</h4>
                <p className="text-xs text-gray-500 mt-1">{announcement.date}</p>
              </div>
            ))}
          </div>
        </Card>
      </div>

      {/* Quick Links */}
      <Card title="Quick Links">
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          <Button variant="ghost" className="w-full justify-center">
            <BookOpen className="w-5 h-5 mr-2" />
            My Courses
          </Button>
          <Button variant="ghost" className="w-full justify-center">
            <FileText className="w-5 h-5 mr-2" />
            Materials
          </Button>
          <Button variant="ghost" className="w-full justify-center">
            <CheckCircle className="w-5 h-5 mr-2" />
            Results
          </Button>
          <Button variant="ghost" className="w-full justify-center">
            <Clock className="w-5 h-5 mr-2" />
            Exams
          </Button>
        </div>
      </Card>
    </Layout>
  );
};

export default StudentDashboard;

