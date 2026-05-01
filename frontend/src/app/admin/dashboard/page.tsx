"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import StatCard from "@/components/ui/StatCard";
import api from "@/lib/api";
import { Users, BookOpen, GraduationCap, Building2, FileText, Calendar } from "lucide-react";

export default function AdminDashboard() {
  const [stats, setStats] = useState<any>(null);
  const [users, setUsers] = useState<any[]>([]);

  useEffect(() => {
    api.get("/admin/stats").then(r => setStats(r.data)).catch(() => {});
    api.get("/admin/users").then(r => setUsers(r.data.slice(0, 6))).catch(() => {});
  }, []);

  return (
    <DashboardLayout title="Admin Dashboard">
      <div className="mb-6">
        <h2 className="text-xl font-semibold text-gray-800">Admin Overview</h2>
        <p className="text-gray-500 text-sm mt-1">Manage your institution's data and users.</p>
      </div>

      {/* Stats */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
        <StatCard title="Total Students"  value={stats?.totalStudents  ?? "—"} icon={<GraduationCap className="w-5 h-5" />} color="blue" />
        <StatCard title="Total Faculty"   value={stats?.totalFaculty   ?? "—"} icon={<Users className="w-5 h-5" />} color="green" />
        <StatCard title="Courses"         value={stats?.totalCourses   ?? "—"} icon={<BookOpen className="w-5 h-5" />} color="yellow" />
        <StatCard title="Departments"     value={stats?.totalDepartments ?? "—"} icon={<Building2 className="w-5 h-5" />} color="purple" />
      </div>
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
        <StatCard title="Total Exams"     value={stats?.totalExams     ?? "—"} icon={<FileText className="w-5 h-5" />} color="yellow" />
        <StatCard title="Total Events"    value={stats?.totalEvents    ?? "—"} icon={<Calendar className="w-5 h-5" />} color="blue" />
        <StatCard title="Total Results"   value={stats?.totalResults   ?? "—"} icon={<FileText className="w-5 h-5" />} color="green" />
        <StatCard title="Total Materials" value={stats?.totalMaterials ?? "—"} icon={<BookOpen className="w-5 h-5" />} color="purple" />
      </div>

      {/* Recent users */}
      <div className="card">
        <div className="flex items-center justify-between mb-4">
          <h3 className="font-semibold text-gray-800">Recent Users</h3>
          <div className="flex gap-2">
            <a href="/admin/students" className="text-primary-600 text-sm hover:underline">Students</a>
            <span className="text-gray-300">·</span>
            <a href="/admin/faculty"  className="text-primary-600 text-sm hover:underline">Faculty</a>
          </div>
        </div>
        {users.length === 0 ? (
          <p className="text-gray-400 text-sm text-center py-8">No users found</p>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-gray-100">
                  <th className="text-left py-2 px-3 text-gray-500 font-medium">Name</th>
                  <th className="text-left py-2 px-3 text-gray-500 font-medium">Email</th>
                  <th className="text-left py-2 px-3 text-gray-500 font-medium">Role</th>
                  <th className="text-left py-2 px-3 text-gray-500 font-medium">Department</th>
                  <th className="text-left py-2 px-3 text-gray-500 font-medium">Status</th>
                </tr>
              </thead>
              <tbody>
                {users.map((u) => (
                  <tr key={u.id} className="border-b border-gray-50 hover:bg-gray-50 transition-colors">
                    <td className="py-2.5 px-3 font-medium text-gray-800">{u.firstName} {u.lastName}</td>
                    <td className="py-2.5 px-3 text-gray-500">{u.email}</td>
                    <td className="py-2.5 px-3">
                      <span className={`badge ${u.role === "FACULTY" ? "bg-blue-100 text-blue-700" : "bg-green-100 text-green-700"}`}>
                        {u.role}
                      </span>
                    </td>
                    <td className="py-2.5 px-3 text-gray-500">{u.department ?? "—"}</td>
                    <td className="py-2.5 px-3">
                      <span className={`badge ${u.enabled ? "bg-green-100 text-green-700" : "bg-red-100 text-red-700"}`}>
                        {u.enabled ? "Active" : "Disabled"}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </DashboardLayout>
  );
}

