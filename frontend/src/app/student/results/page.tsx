"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import api from "@/lib/api";
import { getUser } from "@/lib/auth";
import { gradeColor, formatDate } from "@/lib/utils";
import { BarChart2 } from "lucide-react";

export default function StudentResultsPage() {
  const user              = getUser();
  const [results, setResults] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!user) return;
    api.get(`/results/student/${user.id}`)
       .then(r => setResults(r.data))
       .catch(() => {})
       .finally(() => setLoading(false));
  }, [user]);

  const avg = results.length
    ? (results.reduce((s, r) => s + (r.obtainedMarks / r.totalMarks) * 100, 0) / results.length).toFixed(1)
    : null;

  return (
    <DashboardLayout title="My Results">
      {avg && (
        <div className="mb-6 flex gap-4">
          <div className="card text-center px-8">
            <p className="text-3xl font-bold text-primary-600">{avg}%</p>
            <p className="text-xs text-gray-500 mt-1">Average Score</p>
          </div>
          <div className="card text-center px-8">
            <p className="text-3xl font-bold text-gray-800">{results.length}</p>
            <p className="text-xs text-gray-500 mt-1">Total Results</p>
          </div>
        </div>
      )}

      <div className="card">
        <h3 className="font-semibold text-gray-800 mb-4">All Results</h3>
        {loading ? (
          <div className="flex justify-center py-12"><div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600" /></div>
        ) : results.length === 0 ? (
          <div className="text-center py-10">
            <BarChart2 className="w-10 h-10 text-gray-300 mx-auto mb-2" />
            <p className="text-gray-400">No results published yet</p>
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-gray-100">
                  <th className="text-left py-2 px-3 text-gray-500 font-medium">Course</th>
                  <th className="text-left py-2 px-3 text-gray-500 font-medium">Exam</th>
                  <th className="text-left py-2 px-3 text-gray-500 font-medium">Marks</th>
                  <th className="text-left py-2 px-3 text-gray-500 font-medium">Grade</th>
                  <th className="text-left py-2 px-3 text-gray-500 font-medium">Status</th>
                  <th className="text-left py-2 px-3 text-gray-500 font-medium">Date</th>
                </tr>
              </thead>
              <tbody>
                {results.map(r => (
                  <tr key={r.id} className="border-b border-gray-50 hover:bg-gray-50">
                    <td className="py-2.5 px-3 text-gray-700">{r.courseCode}</td>
                    <td className="py-2.5 px-3 text-gray-700">{r.examCode}</td>
                    <td className="py-2.5 px-3 font-medium text-gray-800">{r.obtainedMarks} / {r.totalMarks}</td>
                    <td className="py-2.5 px-3">
                      <span className={`badge ${gradeColor(r.grade)}`}>{r.grade}</span>
                    </td>
                    <td className="py-2.5 px-3">
                      <span className={`badge ${r.status === "PASSED" ? "bg-green-100 text-green-700" : "bg-red-100 text-red-700"}`}>
                        {r.status}
                      </span>
                    </td>
                    <td className="py-2.5 px-3 text-gray-500">{r.resultDate ? formatDate(r.resultDate) : "—"}</td>
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

