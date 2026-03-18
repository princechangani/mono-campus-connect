import React from 'react';
import { Layout } from '@/components/layout/Layout';
import { Card, Button, DataTable } from '@/components/ui/Common';
import { Users, Clock, BookOpen, CheckCircle } from 'lucide-react';

const FacultyDashboard = () => {
  const navItems = [
    { label: 'Dashboard', href: '/faculty/dashboard' },
    { label: 'Courses', href: '/faculty/courses' },
    { label: 'Attendance', href: '/faculty/attendance' },
    { label: 'Results', href: '/faculty/results' },
    { label: 'Exams', href: '/faculty/exams' },
    { label: 'Materials', href: '/faculty/materials' },
  ];

  const myClasses = [
    { id: 1, courseName: 'Data Structures', batchName: 'CSE-2024-A', students: 45, status: 'Active' },
    { id: 2, courseName: 'Database Systems', batchName: 'CSE-2024-B', students: 42, status: 'Active' },
    { id: 3, courseName: 'Web Development', batchName: 'CSE-2024-C', students: 38, status: Active' },
  ];

  const recentAttendance = [
    { id: 1, courseName: 'Data Structures', date: '2026-03-19', present: 42, absent: 3 },
    { id: 2, courseName: 'Database Systems', date: '2026-03-19', present: 40, absent: 2 },
    { id: 3, courseName: 'Web Development', date: '2026-03-19', present: 36, absent: 2 },
  ];

  return (
    <Layout navItems={navItems} title="Faculty Dashboard">
      {/* Welcome Section */}
      <div className="mb-8">
        <h2 className="text-2xl font-bold text-gray-900 mb-2">Welcome Back, Dr. Faculty!</h2>
        <p className="text-gray-600">Your teaching dashboard for this semester</p>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        <Card>
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-600 text-sm">Teaching Classes</p>
              <p className="text-3xl font-bold mt-2">3</p>
            </div>
            <BookOpen className="w-8 h-8 text-blue-500" />
          </div>
        </Card>

        <Card>
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-600 text-sm">Total Students</p>
              <p className="text-3xl font-bold mt-2">125</p>
            </div>
            <Users className="w-8 h-8 text-green-500" />
          </div>
        </Card>

        <Card>
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-600 text-sm">Avg. Attendance</p>
              <p className="text-3xl font-bold mt-2">92%</p>
            </div>
            <CheckCircle className="w-8 h-8 text-purple-500" />
          </div>
        </Card>

        <Card>
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-600 text-sm">Pending Grading</p>
              <p className="text-3xl font-bold mt-2">28</p>
            </div>
            <Clock className="w-8 h-8 text-orange-500" />
          </div>
        </Card>
      </div>

      {/* My Classes */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-8">
        <div className="lg:col-span-2">
          <Card title="My Classes This Semester">
            <div className="space-y-3">
              {myClasses.map((cls) => (
                <div key={cls.id} className="p-4 bg-gray-50 rounded-lg">
                  <div className="flex justify-between items-start mb-2">
                    <div>
                      <h4 className="font-medium text-gray-900">{cls.courseName}</h4>
                      <p className="text-sm text-gray-600">{cls.batchName}</p>
                    </div>
                    <span className="px-2 py-1 bg-green-100 text-green-800 text-xs rounded-full">
                      {cls.status}
                    </span>
                  </div>
                  <div className="flex justify-between items-center">
                    <span className="text-sm text-gray-600">{cls.students} students</span>
                    <Button variant="secondary" size="sm">
                      Manage
                    </Button>
                  </div>
                </div>
              ))}
            </div>
          </Card>
        </div>

        {/* Quick Actions */}
        <Card title="Quick Actions">
          <div className="space-y-2">
            <Button className="w-full justify-start">
              <Clock className="w-4 h-4 mr-2" />
              Take Attendance
            </Button>
            <Button className="w-full justify-start" variant="secondary">
              <FileText className="w-4 h-4 mr-2" />
              Grade Assignment
            </Button>
            <Button className="w-full justify-start" variant="secondary">
              <BookOpen className="w-4 h-4 mr-2" />
              Upload Material
            </Button>
            <Button className="w-full justify-start" variant="secondary">
              <Users className="w-4 h-4 mr-2" />
              View Results
            </Button>
          </div>
        </Card>
      </div>

      {/* Recent Attendance */}
      <Card title="Recent Attendance Records">
        <DataTable
          columns={[
            { key: 'courseName', label: 'Course' },
            { key: 'date', label: 'Date' },
            { key: 'present', label: 'Present' },
            { key: 'absent', label: 'Absent' },
            {
              key: 'percentage',
              label: 'Percentage',
              render: (_, item: any) => {
                const total = item.present + item.absent;
                const pct = ((item.present / total) * 100).toFixed(1);
                return `${pct}%`;
              },
            },
          ]}
          data={recentAttendance}
        />
      </Card>
    </Layout>
  );
};

export default FacultyDashboard;

