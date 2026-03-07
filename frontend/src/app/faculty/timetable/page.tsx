"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import api from "@/lib/api";
import { Clock } from "lucide-react";

const DAYS = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"];
const DAY_COLORS: Record<string, string> = {
  MONDAY:    "border-blue-200   bg-blue-50",
  TUESDAY:   "border-green-200  bg-green-50",
  WEDNESDAY: "border-yellow-200 bg-yellow-50",
  THURSDAY:  "border-purple-200 bg-purple-50",
  FRIDAY:    "border-pink-200   bg-pink-50",
  SATURDAY:  "border-orange-200 bg-orange-50",
};

export default function FacultyTimetablePage() {
  const [entries,  setEntries]  = useState<any[]>([]);
  const [semesters, setSemesters] = useState<string[]>([]);
  const [selSem,   setSelSem]   = useState("");
  const [loading,  setLoading]  = useState(true);

  useEffect(() => {
    api.get("/timetable")
      .then(r => {
        setEntries(r.data);
        const uniq = [...new Set<string>(r.data.map((e: any) => e.semester))].sort();
        setSemesters(uniq);
        if (uniq.length > 0) setSelSem(uniq[0]);
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);

  const filtered = selSem ? entries.filter(e => e.semester === selSem) : entries;
  const byDay    = DAYS.reduce<Record<string, any[]>>((acc, d) => {
    acc[d] = filtered.filter(e => e.dayOfWeek === d).sort((a, b) => a.timeSlot.localeCompare(b.timeSlot));
    return acc;
  }, {});

  const today = new Date().toLocaleDateString("en-US", { weekday: "long" }).toUpperCase();

  return (
    <DashboardLayout title="Timetable">
      <div className="flex items-center justify-between mb-6 flex-wrap gap-3">
        <div>
          <h2 className="text-xl font-semibold text-gray-800">Class Timetable</h2>
          <p className="text-sm text-gray-400 mt-0.5">Today is <span className="font-medium text-primary-600">{today}</span></p>
        </div>
        <select className="input w-40" value={selSem} onChange={e => setSelSem(e.target.value)}>
          <option value="">All Semesters</option>
          {semesters.map(s => <option key={s} value={s}>Semester {s}</option>)}
        </select>
      </div>

      {loading ? (
        <div className="flex justify-center py-16">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600" />
        </div>
      ) : filtered.length === 0 ? (
        <div className="card text-center py-14">
          <Clock className="w-10 h-10 text-gray-300 mx-auto mb-3" />
          <p className="text-gray-400">No timetable entries found</p>
        </div>
      ) : (
        <div className="space-y-4">
          {DAYS.map(day => byDay[day].length > 0 && (
            <div key={day} className={`rounded-xl border p-4 ${DAY_COLORS[day]} ${day === today ? "ring-2 ring-primary-400" : ""}`}>
              <div className="flex items-center gap-2 mb-3">
                <h3 className="font-semibold text-gray-700 text-sm uppercase tracking-wide">{day}</h3>
                {day === today && <span className="badge bg-primary-600 text-white text-xs">Today</span>}
              </div>
              <div className="grid gap-2 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
                {byDay[day].map(entry => (
                  <div key={entry.id} className="bg-white rounded-lg p-3 shadow-sm border border-white flex justify-between items-start">
                    <div className="min-w-0 flex-1">
                      <p className="text-sm font-semibold text-gray-800 truncate">{entry.courseName ?? entry.courseCode}</p>
                      <p className="text-xs text-primary-600 font-mono">{entry.courseCode}</p>
                      {entry.roomNumber && <p className="text-xs text-gray-400 mt-0.5">📍 Room {entry.roomNumber}</p>}
                    </div>
                    <div className="text-right ml-2 flex-shrink-0">
                      <p className="text-xs font-bold text-gray-700">{entry.timeSlot}</p>
                      <p className="text-xs text-gray-400">Sem {entry.semester}</p>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      )}
    </DashboardLayout>
  );
}

