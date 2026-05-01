"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import api from "@/lib/api";
import { formatDate } from "@/lib/utils";
import { FileText } from "lucide-react";

const typeColors: Record<string, string> = {
  MIDTERM:    "bg-yellow-100 text-yellow-700",
  FINAL:      "bg-red-100 text-red-700",
  QUIZ:       "bg-blue-100 text-blue-700",
  ASSIGNMENT: "bg-green-100 text-green-700",
};

export default function StudentExamsPage() {
  const [exams,    setExams]   = useState<any[]>([]);
  const [tab,      setTab]     = useState<"upcoming" | "past">("upcoming");
  const [loading,  setLoading] = useState(true);

  useEffect(() => {
    api.get("/exams").then(r => setExams(r.data)).catch(() => {}).finally(() => setLoading(false));
  }, []);

  const now      = new Date();
  const upcoming = exams.filter(e => new Date(e.startDate) >= now);
  const past     = exams.filter(e => new Date(e.startDate) < now);
  const shown    = tab === "upcoming" ? upcoming : past;

  return (
    <DashboardLayout title="Exams">
      <div className="flex items-center justify-between mb-6">
        <h2 className="text-xl font-semibold text-gray-800">Exams</h2>
        <div className="flex gap-1 bg-gray-100 p-1 rounded-lg">
          {(["upcoming", "past"] as const).map(t => (
            <button key={t} onClick={() => setTab(t)}
              className={`px-4 py-1.5 rounded-md text-sm font-medium capitalize transition-all ${tab === t ? "bg-white shadow text-gray-800" : "text-gray-500"}`}>
              {t} {t === "upcoming" ? `(${upcoming.length})` : `(${past.length})`}
            </button>
          ))}
        </div>
      </div>

      {loading ? (
        <div className="flex justify-center py-12"><div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600" /></div>
      ) : shown.length === 0 ? (
        <div className="card text-center py-12">
          <FileText className="w-10 h-10 text-gray-300 mx-auto mb-3" />
          <p className="text-gray-400">No {tab} exams</p>
        </div>
      ) : (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {shown.map(e => (
            <div key={e.id} className="card hover:shadow-md transition-shadow">
              <div className="flex items-start justify-between mb-3">
                <span className={`badge ${typeColors[e.type] ?? "bg-gray-100 text-gray-700"}`}>{e.type}</span>
                <span className="text-xs text-gray-400">{e.courseCode}</span>
              </div>
              <h3 className="font-semibold text-gray-800 mb-1">{e.title}</h3>
              <p className="text-sm text-gray-500 line-clamp-2 mb-3">{e.description}</p>
              <div className="border-t border-gray-100 pt-3 text-xs text-gray-500 space-y-1">
                <div className="flex justify-between">
                  <span>Start</span><span className="font-medium text-gray-700">{formatDate(e.startDate)}</span>
                </div>
                <div className="flex justify-between">
                  <span>End</span><span className="font-medium text-gray-700">{formatDate(e.endDate)}</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </DashboardLayout>
  );
}

