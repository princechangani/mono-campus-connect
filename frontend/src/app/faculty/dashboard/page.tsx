"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import StatCard from "@/components/ui/StatCard";
import api from "@/lib/api";
import { getUser } from "@/lib/auth";
import { Users, BookOpen, FileText, BarChart2, ClipboardList } from "lucide-react";
import { formatDate } from "@/lib/utils";

export default function FacultyDashboard() {
  const user = getUser();
  const [courses,  setCourses]  = useState<any[]>([]);
  const [exams,    setExams]    = useState<any[]>([]);
  const [results,  setResults]  = useState<any[]>([]);
  const [materials,setMaterials]= useState<any[]>([]);

  useEffect(() => {
    api.get("/courses").then(r => setCourses(r.data)).catch(() => {});
    api.get("/exams").then(r => setExams(r.data)).catch(() => {});
    api.get("/results").then(r => setResults(r.data)).catch(() => {});
    api.get("/materials/recent/5").then(r => setMaterials(r.data)).catch(() => {});
  }, []);

  return (
    <DashboardLayout title="Faculty Dashboard">
      <div className="mb-6">
        <h2 className="text-xl font-semibold text-gray-800">
          Welcome, Prof. {user?.firstName ?? "Faculty"} 👋
        </h2>
        <p className="text-gray-500 text-sm mt-1">Manage your courses, exams and student results.</p>
      </div>

      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
        <StatCard title="My Courses"     value={courses.length}   icon={<BookOpen className="w-5 h-5" />} color="blue" />
        <StatCard title="Active Exams"   value={exams.length}     icon={<FileText className="w-5 h-5" />} color="yellow" />
        <StatCard title="Results Posted" value={results.length}   icon={<BarChart2 className="w-5 h-5" />} color="green" />
        <StatCard title="Materials"      value={materials.length} icon={<ClipboardList className="w-5 h-5" />} color="purple" />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Courses */}
        <div className="card">
          <div className="flex items-center justify-between mb-4">
            <h3 className="font-semibold text-gray-800">My Courses</h3>
            <a href="/faculty/courses" className="text-primary-600 text-sm hover:underline">Manage</a>
          </div>
          {courses.length === 0 ? (
            <p className="text-gray-400 text-sm text-center py-6">No courses assigned</p>
          ) : (
            <div className="space-y-2">
              {courses.slice(0, 5).map((c) => (
                <div key={c.id} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                  <div>
                    <p className="text-sm font-medium text-gray-800">{c.courseName}</p>
                    <p className="text-xs text-gray-500">{c.courseCode} · {c.department}</p>
                  </div>
                  <span className="badge bg-blue-100 text-blue-700">{c.semester}</span>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Exams */}
        <div className="card">
          <div className="flex items-center justify-between mb-4">
            <h3 className="font-semibold text-gray-800">Exams</h3>
            <a href="/faculty/exams" className="text-primary-600 text-sm hover:underline">Manage</a>
          </div>
          {exams.length === 0 ? (
            <p className="text-gray-400 text-sm text-center py-6">No exams created</p>
          ) : (
            <div className="space-y-2">
              {exams.slice(0, 5).map((e) => (
                <div key={e.id} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                  <div>
                    <p className="text-sm font-medium text-gray-800">{e.title}</p>
                    <p className="text-xs text-gray-500">{e.courseCode}</p>
                  </div>
                  <div className="text-right">
                    <span className="badge bg-yellow-100 text-yellow-700">{e.type}</span>
                    <p className="text-xs text-gray-400 mt-0.5">{formatDate(e.startDate)}</p>
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
            <a href="/faculty/materials" className="text-primary-600 text-sm hover:underline">Manage</a>
          </div>
          {materials.length === 0 ? (
            <p className="text-gray-400 text-sm text-center py-6">No materials uploaded</p>
          ) : (
            <div className="space-y-2">
              {materials.map((m) => (
                <div key={m.id} className="flex items-center gap-3 p-3 bg-gray-50 rounded-lg">
                  <div className="w-8 h-8 bg-primary-100 rounded-lg flex items-center justify-center">
                    <BookOpen className="w-4 h-4 text-primary-600" />
                  </div>
                  <div className="flex-1 min-w-0">
                    <p className="text-sm font-medium text-gray-800 truncate">{m.title}</p>
                    <p className="text-xs text-gray-500">{m.type}</p>
                  </div>
                  <span className="text-xs text-gray-400">{m.downloadCount} downloads</span>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Latest Results */}
        <div className="card">
          <div className="flex items-center justify-between mb-4">
            <h3 className="font-semibold text-gray-800">Recent Results</h3>
            <a href="/faculty/results" className="text-primary-600 text-sm hover:underline">Manage</a>
          </div>
          {results.length === 0 ? (
            <p className="text-gray-400 text-sm text-center py-6">No results posted</p>
          ) : (
            <div className="space-y-2">
              {results.slice(0, 5).map((r) => (
                <div key={r.id} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                  <div>
                    <p className="text-sm font-medium text-gray-800">Student {r.studentId}</p>
                    <p className="text-xs text-gray-500">{r.courseCode}</p>
                  </div>
                  <div className="text-right">
                    <span className={`badge ${r.grade === "F" ? "bg-red-100 text-red-700" : "bg-green-100 text-green-700"}`}>
                      {r.grade}
                    </span>
                    <p className="text-xs text-gray-400 mt-0.5">{r.obtainedMarks}/{r.totalMarks}</p>
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

