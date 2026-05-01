"use client";
import React, { useState, useEffect } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import { getUser } from "@/lib/auth";
import { courseAssignmentService } from "@/lib/services/courseAssignmentService";
import { BookOpen, Search, Filter, Eye, Users, Clock } from "lucide-react";

interface CourseAssignmentItem {
  id?: number;
  courseId: number;
  facultyId: number;
  batchId: number;
  academicYearId?: number;
  semesterNumber?: number;
  status?: string;
  tenantId?: string;
}

export default function HodCourses() {
  const user = getUser();
  const [courses, setCourses] = useState<CourseAssignmentItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [semesterFilter, setSemesterFilter] = useState("all");

  useEffect(() => {
    fetchCourses();
  }, []);

  const fetchCourses = async () => {
    try {
      setLoading(true);
      const response = await courseAssignmentService.getAll();
      setCourses(response.data);
    } catch (error) {
      console.error("Failed to fetch courses:", error);
    } finally {
      setLoading(false);
    }
  };

  const filteredCourses = courses.filter(course => {
    const matchesSearch = course.courseId.toString().includes(searchTerm.toLowerCase()) ||
                         course.facultyId.toString().includes(searchTerm.toLowerCase());
    const matchesSemester = semesterFilter === "all" ||
      String(course.semesterNumber ?? "").toString() === semesterFilter;
    return matchesSearch && matchesSemester;
  });

  const getUniqueSemesters = () => {
    const semesters = new Set(courses.map(c => c.semesterNumber).filter(s => s != null) as number[]);
    return Array.from(semesters).sort((a, b) => a - b);
  };

  return (
    <DashboardLayout title="Courses Management">
      <div className="mb-6">
        <h2 className="text-xl font-semibold text-gray-800">
          Department Courses - {user?.firstName ?? "HOD"}
        </h2>
        <p className="text-gray-500 text-sm mt-1">View and manage course assignments in your department.</p>
      </div>

      {/* Filters */}
      <div className="card p-4 mb-6">
        <div className="flex flex-col sm:flex-row gap-4">
          <div className="flex-1">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
              <input
                type="text"
                placeholder="Search by course or faculty..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
              />
            </div>
          </div>
          <div className="flex items-center gap-2">
            <Filter className="w-4 h-4 text-gray-500" />
            <select
              value={semesterFilter}
              onChange={(e) => setSemesterFilter(e.target.value)}
              className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
            >
              <option value="all">All Semesters</option>
              {getUniqueSemesters().map(semester => (
                <option key={semester} value={semester}>
                  Semester {semester}
                </option>
              ))}
            </select>
          </div>
        </div>
      </div>

      {/* Courses Table */}
      <div className="card">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b border-gray-200">
                <th className="text-left py-3 px-4 font-medium text-gray-700">Course ID</th>
                <th className="text-left py-3 px-4 font-medium text-gray-700">Course Name</th>
                <th className="text-left py-3 px-4 font-medium text-gray-700">Faculty</th>
                <th className="text-left py-3 px-4 font-medium text-gray-700">Batch</th>
                <th className="text-left py-3 px-4 font-medium text-gray-700">Semester</th>
                <th className="text-left py-3 px-4 font-medium text-gray-700">Status</th>
                <th className="text-left py-3 px-4 font-medium text-gray-700">Actions</th>
              </tr>
            </thead>
            <tbody>
              {loading ? (
                <tr>
                  <td colSpan={7} className="text-center py-8 text-gray-500">
                    Loading courses...
                  </td>
                </tr>
              ) : filteredCourses.length === 0 ? (
                <tr>
                  <td colSpan={7} className="text-center py-8 text-gray-500">
                    No courses found
                  </td>
                </tr>
              ) : (
                filteredCourses.map((course) => (
                  <tr key={course.id} className="border-b border-gray-100 hover:bg-gray-50">
                    <td className="py-3 px-4">
                      <span className="font-medium text-gray-900">{course.courseId}</span>
                    </td>
                    <td className="py-3 px-4 text-gray-700">
                      {/* Will be populated when course data is joined */}
                      Course Name
                    </td>
                    <td className="py-3 px-4 text-gray-700">
                      <div className="flex items-center gap-2">
                        <Users className="w-4 h-4 text-gray-400" />
                        Faculty {course.facultyId}
                      </div>
                    </td>
                    <td className="py-3 px-4 text-gray-700">Batch {course.batchId}</td>
                    <td className="py-3 px-4 text-gray-700">
                      <div className="flex items-center gap-2">
                        <Clock className="w-4 h-4 text-gray-400" />
                        Semester {course.semesterNumber ?? "—"}
                      </div>
                    </td>
                    <td className="py-3 px-4">
                      <span className={`inline-flex px-2 py-1 text-xs font-medium rounded-full ${
                        course.status === "ACTIVE" 
                          ? "bg-green-100 text-green-800"
                          : course.status === "COMPLETED"
                          ? "bg-blue-100 text-blue-800"
                          : "bg-gray-100 text-gray-800"
                      }`}>
                        {course.status || "ACTIVE"}
                      </span>
                    </td>
                    <td className="py-3 px-4">
                      <button className="text-primary-600 hover:text-primary-700 p-1">
                        <Eye className="w-4 h-4" />
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </DashboardLayout>
  );
}
