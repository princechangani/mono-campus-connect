"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import api from "@/lib/api";
import { getUser } from "@/lib/auth";
import { BookOpen, Users, Hash } from "lucide-react";

const TYPE_BADGE: Record<string, string> = {
  COMPULSORY: "bg-blue-100 text-blue-700",
  OPTIONAL:   "bg-amber-100 text-amber-700",
};
const CAT_BADGE: Record<string, string> = {
  Core:     "bg-green-100 text-green-700",
  Elective: "bg-purple-100 text-purple-700",
  Lab:      "bg-cyan-100 text-cyan-700",
  Project:  "bg-rose-100 text-rose-700",
  Seminar:  "bg-orange-100 text-orange-700",
};

export default function FacultyCoursesPage() {
  const [courses,  setCourses]  = useState<any[]>([]);
  const [loading,  setLoading]  = useState(true);
  const user = getUser();

  useEffect(() => {
    // fetch all courses then filter by this faculty's name or id
    api.get("/courses")
      .then(r => {
        const all: any[] = r.data;
        const mine = all.filter(c =>
          c.instructor === `${user?.firstName} ${user?.lastName}` ||
          c.facultyId  === user?.facultyId
        );
        setCourses(mine.length > 0 ? mine : all); // fallback: show all if no match
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);

  return (
    <DashboardLayout title="My Subjects">
      <div className="flex items-center justify-between mb-6">
        <div>
          <h2 className="text-xl font-semibold text-gray-800">My Subjects</h2>
          <p className="text-sm text-gray-400 mt-0.5">Subjects assigned to you this semester</p>
        </div>
        <span className="badge bg-primary-100 text-primary-700 text-sm px-3 py-1">
          {courses.length} Subject{courses.length !== 1 ? "s" : ""}
        </span>
      </div>

      {loading ? (
        <div className="flex justify-center py-16">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600" />
        </div>
      ) : courses.length === 0 ? (
        <div className="card text-center py-14">
          <BookOpen className="w-10 h-10 text-gray-300 mx-auto mb-3" />
          <p className="text-gray-400">No subjects assigned yet</p>
        </div>
      ) : (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {courses.map(c => (
            <div key={c.id} className="card hover:shadow-md transition-shadow !p-0 overflow-hidden">
              <div className={`h-1.5 ${
                c.semester === "2" ? "bg-green-400" :
                c.semester === "4" ? "bg-blue-400" :
                c.semester === "6" ? "bg-purple-400" : "bg-primary-400"
              }`} />
              <div className="p-5">
                <div className="flex flex-wrap gap-1 mb-3">
                  <span className="badge bg-gray-100 text-gray-600 font-mono text-xs">{c.courseCode}</span>
                  <span className="badge bg-sky-50 text-sky-700 text-xs">Sem {c.semester}</span>
                  {c.subjectType && (
                    <span className={`badge text-xs ${TYPE_BADGE[c.subjectType] ?? "bg-gray-100 text-gray-600"}`}>
                      {c.subjectType === "COMPULSORY" ? "Compulsory" : "Optional"}
                    </span>
                  )}
                  {c.category && (
                    <span className={`badge text-xs ${CAT_BADGE[c.category] ?? "bg-gray-100 text-gray-600"}`}>
                      {c.category}
                    </span>
                  )}
                </div>

                <h3 className="font-semibold text-gray-800 leading-snug">{c.courseName}</h3>
                <p className="text-sm text-gray-500 mt-0.5">{c.department}</p>

                <div className="grid grid-cols-2 gap-3 mt-4 pt-4 border-t border-gray-100">
                  <div className="flex items-center gap-2 text-sm text-gray-600">
                    <Hash className="w-4 h-4 text-gray-400" />
                    <span>{c.credits} Credits</span>
                  </div>
                  <div className="flex items-center gap-2 text-sm text-gray-600">
                    <Users className="w-4 h-4 text-gray-400" />
                    <span>Semester {c.semester}</span>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </DashboardLayout>
  );
}

