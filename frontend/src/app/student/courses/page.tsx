"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import api from "@/lib/api";
import { BookOpen } from "lucide-react";

export default function StudentCoursesPage() {
  const [courses, setCourses] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get("/courses").then(r => setCourses(r.data)).catch(() => {}).finally(() => setLoading(false));
  }, []);

  return (
    <DashboardLayout title="Courses">
      <div className="mb-6">
        <h2 className="text-xl font-semibold text-gray-800">My Courses</h2>
        <p className="text-gray-500 text-sm mt-1">{courses.length} courses available</p>
      </div>

      {loading ? (
        <div className="flex justify-center py-12"><div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600" /></div>
      ) : courses.length === 0 ? (
        <div className="card text-center py-12">
          <BookOpen className="w-10 h-10 text-gray-300 mx-auto mb-3" />
          <p className="text-gray-400">No courses found</p>
        </div>
      ) : (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {courses.map(c => (
            <div key={c.id} className="card hover:shadow-md transition-shadow border-t-4 border-primary-400">
              <div className="flex items-start justify-between mb-2">
                <span className="badge bg-primary-100 text-primary-700">{c.courseCode}</span>
                <span className="badge bg-gray-100 text-gray-600">{c.credits} credits</span>
              </div>
              <h3 className="font-semibold text-gray-800 mt-2">{c.courseName}</h3>
              <p className="text-xs text-gray-500 mt-1">{c.department}</p>
              <div className="flex items-center justify-between mt-3 pt-3 border-t border-gray-100 text-xs text-gray-500">
                <span>👨‍🏫 {c.instructor ?? "TBA"}</span>
                <span>Sem {c.semester}</span>
              </div>
            </div>
          ))}
        </div>
      )}
    </DashboardLayout>
  );
}

