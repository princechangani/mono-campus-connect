"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import StatCard from "@/components/ui/StatCard";
import api from "@/lib/api";
import { getUser } from "@/lib/auth";
import { BookOpen, ClipboardList, FileText, BarChart2, Bell, Clock } from "lucide-react";
import { formatDate } from "@/lib/utils";

export default function StudentDashboard() {
  const user = getUser();
  const [exams,     setExams]     = useState<any[]>([]);
  const [materials, setMaterials] = useState<any[]>([]);
  const [events,    setEvents]    = useState<any[]>([]);
  const [notifs,    setNotifs]    = useState<any[]>([]);

  useEffect(() => {
    api.get("/exams").then(r => setExams(r.data)).catch(() => {});
    api.get("/materials/recent/5").then(r => setMaterials(r.data)).catch(() => {});
    api.get("/events").then(r => setEvents(r.data.slice(0, 3))).catch(() => {});
    api.get("/notifications/unread").then(r => setNotifs(r.data.slice(0, 5))).catch(() => {});
  }, []);

  return (
    <DashboardLayout title="Student Dashboard">
      {/* Welcome */}
      <div className="mb-6">
        <h2 className="text-xl font-semibold text-gray-800">
          Good morning, {user?.firstName ?? "Student"} 👋
        </h2>
        <p className="text-gray-500 text-sm mt-1">Here's what's happening today.</p>
      </div>

      {/* Stats */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
        <StatCard title="Enrolled Courses" value={exams.length}  icon={<BookOpen className="w-5 h-5" />} color="blue" />
        <StatCard title="Upcoming Exams"   value={exams.filter(e => new Date(e.startDate) > new Date()).length} icon={<FileText className="w-5 h-5" />} color="yellow" />
        <StatCard title="Study Materials"  value={materials.length} icon={<ClipboardList className="w-5 h-5" />} color="green" />
        <StatCard title="Notifications"    value={notifs.length}    icon={<Bell className="w-5 h-5" />} color="purple" />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Upcoming Exams */}
        <div className="card">
          <div className="flex items-center justify-between mb-4">
            <h3 className="font-semibold text-gray-800">Upcoming Exams</h3>
            <a href="/student/exams" className="text-primary-600 text-sm hover:underline">View all</a>
          </div>
          {exams.filter(e => new Date(e.startDate) > new Date()).length === 0 ? (
            <p className="text-gray-400 text-sm text-center py-6">No upcoming exams</p>
          ) : (
            <div className="space-y-3">
              {exams.filter(e => new Date(e.startDate) > new Date()).slice(0, 4).map((exam) => (
                <div key={exam.id} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                  <div>
                    <p className="text-sm font-medium text-gray-800">{exam.title}</p>
                    <p className="text-xs text-gray-500">{exam.courseCode}</p>
                  </div>
                  <div className="text-right">
                    <span className="badge bg-yellow-100 text-yellow-700">{exam.type}</span>
                    <p className="text-xs text-gray-500 mt-1">{formatDate(exam.startDate)}</p>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Recent Materials */}
        <div className="card">
          <div className="flex items-center justify-between mb-4">
            <h3 className="font-semibold text-gray-800">Recent Materials</h3>
            <a href="/student/materials" className="text-primary-600 text-sm hover:underline">View all</a>
          </div>
          {materials.length === 0 ? (
            <p className="text-gray-400 text-sm text-center py-6">No materials uploaded yet</p>
          ) : (
            <div className="space-y-3">
              {materials.map((m) => (
                <div key={m.id} className="flex items-center gap-3 p-3 bg-gray-50 rounded-lg">
                  <div className="w-9 h-9 bg-primary-100 rounded-lg flex items-center justify-center">
                    <BookOpen className="w-4 h-4 text-primary-600" />
                  </div>
                  <div className="flex-1 min-w-0">
                    <p className="text-sm font-medium text-gray-800 truncate">{m.title}</p>
                    <p className="text-xs text-gray-500">{m.courseCode} · {m.type}</p>
                  </div>
                  <span className="text-xs text-gray-400">{formatDate(m.uploadedDate)}</span>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Events */}
        <div className="card">
          <div className="flex items-center justify-between mb-4">
            <h3 className="font-semibold text-gray-800">Latest Events</h3>
            <a href="/student/events" className="text-primary-600 text-sm hover:underline">View all</a>
          </div>
          {events.length === 0 ? (
            <p className="text-gray-400 text-sm text-center py-6">No events</p>
          ) : (
            <div className="space-y-3">
              {events.map((e) => (
                <div key={e.id} className="p-3 bg-gray-50 rounded-lg">
                  <p className="text-sm font-medium text-gray-800">{e.title}</p>
                  <p className="text-xs text-gray-500 mt-1 line-clamp-2">{e.description}</p>
                  <p className="text-xs text-gray-400 mt-1">{formatDate(e.createdAt)}</p>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Notifications */}
        <div className="card">
          <div className="flex items-center justify-between mb-4">
            <h3 className="font-semibold text-gray-800">Notifications</h3>
            <a href="/notifications" className="text-primary-600 text-sm hover:underline">View all</a>
          </div>
          {notifs.length === 0 ? (
            <p className="text-gray-400 text-sm text-center py-6">All caught up! 🎉</p>
          ) : (
            <div className="space-y-2">
              {notifs.map((n) => (
                <div key={n.id} className="flex gap-3 p-3 bg-blue-50 rounded-lg border-l-4 border-primary-400">
                  <Bell className="w-4 h-4 text-primary-500 mt-0.5 flex-shrink-0" />
                  <div>
                    <p className="text-sm font-medium text-gray-800">{n.title}</p>
                    <p className="text-xs text-gray-500">{n.message}</p>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </DashboardLayout>
  );
}

