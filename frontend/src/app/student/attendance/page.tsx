"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import api from "@/lib/api";
import { getUser, getRole } from "@/lib/auth";
import { formatDate } from "@/lib/utils";
import toast from "react-hot-toast";
import { CheckCircle, XCircle, Clock, BarChart2 } from "lucide-react";

const statusColors: Record<string, string> = {
  PRESENT: "bg-green-100 text-green-700",
  ABSENT:  "bg-red-100 text-red-700",
  LATE:    "bg-yellow-100 text-yellow-700",
};

export default function AttendancePage() {
  const user  = getUser();
  const role  = getRole();
  const [records,  setRecords]  = useState<any[]>([]);
  const [courses,  setCourses]  = useState<any[]>([]);
  const [selected, setSelected] = useState("");
  const [loading,  setLoading]  = useState(true);
  const [pct,      setPct]      = useState<any>(null);

  useEffect(() => {
    api.get("/courses").then(r => setCourses(r.data)).catch(() => {});
  }, []);

  useEffect(() => {
    if (!user) return;
    setLoading(true);
    if (role === "STUDENT") {
      const url = selected
        ? `/attendance/student/${user.id}/course/${selected}`
        : `/attendance/student/${user.id}`;
      api.get(url).then(r => setRecords(r.data)).catch(() => {}).finally(() => setLoading(false));
      if (selected) {
        api.get(`/attendance/percentage/${user.id}/course/${selected}`)
           .then(r => setPct(r.data)).catch(() => setPct(null));
      }
    } else {
      const url = selected ? `/attendance/course/${selected}` : "/attendance/student/1";
      api.get(url).then(r => setRecords(r.data)).catch(() => {}).finally(() => setLoading(false));
    }
  }, [selected, user, role]);

  return (
    <DashboardLayout title="Attendance">
      <div className="flex items-center justify-between mb-6 flex-wrap gap-3">
        <h2 className="text-xl font-semibold text-gray-800">Attendance Records</h2>
        <select className="input w-48" value={selected} onChange={e => setSelected(e.target.value)}>
          <option value="">All Courses</option>
          {courses.map(c => <option key={c.id} value={c.courseCode}>{c.courseCode} – {c.courseName}</option>)}
        </select>
      </div>

      {/* Percentage card */}
      {pct && selected && (
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 mb-6">
          <div className="card text-center">
            <p className="text-3xl font-bold text-gray-800">{pct.percentage}%</p>
            <p className="text-xs text-gray-500 mt-1">Attendance</p>
            {pct.belowThreshold && (
              <span className="badge bg-red-100 text-red-700 mt-2">Below 75% ⚠️</span>
            )}
          </div>
          <div className="card text-center">
            <p className="text-3xl font-bold text-green-600">{pct.presentClasses}</p>
            <p className="text-xs text-gray-500 mt-1">Present</p>
          </div>
          <div className="card text-center">
            <p className="text-3xl font-bold text-red-500">{pct.totalClasses - pct.presentClasses}</p>
            <p className="text-xs text-gray-500 mt-1">Absent</p>
          </div>
          <div className="card text-center">
            <p className="text-3xl font-bold text-gray-800">{pct.totalClasses}</p>
            <p className="text-xs text-gray-500 mt-1">Total Classes</p>
          </div>
        </div>
      )}

      <div className="card">
        {loading ? (
          <div className="flex justify-center py-12">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600" />
          </div>
        ) : records.length === 0 ? (
          <p className="text-gray-400 text-sm text-center py-10">No attendance records found</p>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-gray-100">
                  <th className="text-left py-2 px-3 text-gray-500 font-medium">Date</th>
                  <th className="text-left py-2 px-3 text-gray-500 font-medium">Course</th>
                  <th className="text-left py-2 px-3 text-gray-500 font-medium">Status</th>
                  <th className="text-left py-2 px-3 text-gray-500 font-medium">Remarks</th>
                </tr>
              </thead>
              <tbody>
                {records.map(r => (
                  <tr key={r.id} className="border-b border-gray-50 hover:bg-gray-50">
                    <td className="py-2.5 px-3 text-gray-700">{formatDate(r.sessionDate)}</td>
                    <td className="py-2.5 px-3 text-gray-700">{r.courseCode}</td>
                    <td className="py-2.5 px-3">
                      <span className={`badge ${statusColors[r.status] ?? "bg-gray-100 text-gray-600"}`}>{r.status}</span>
                    </td>
                    <td className="py-2.5 px-3 text-gray-500">{r.remarks ?? "—"}</td>
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

