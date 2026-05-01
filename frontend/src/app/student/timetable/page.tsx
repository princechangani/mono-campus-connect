"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import api from "@/lib/api";
import { Clock } from "lucide-react";

const DAYS = ["MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"];

export default function TimetablePage() {
  const [entries,  setEntries]  = useState<any[]>([]);
  const [semesters, setSemesters] = useState<string[]>([]);
  const [selected,  setSelected]  = useState("");
  const [loading,   setLoading]   = useState(true);

  useEffect(() => {
    api.get("/timetable").then(r => {
      setEntries(r.data);
      const uniq = [...new Set<string>(r.data.map((e: any) => e.semester))];
      setSemesters(uniq);
      if (uniq.length > 0) setSelected(uniq[0]);
    }).catch(() => {}).finally(() => setLoading(false));
  }, []);

  const filtered = selected ? entries.filter(e => e.semester === selected) : entries;

  const byDay = DAYS.reduce<Record<string, any[]>>((acc, d) => {
    acc[d] = filtered.filter(e => e.dayOfWeek === d).sort((a, b) => a.timeSlot.localeCompare(b.timeSlot));
    return acc;
  }, {});

  return (
    <DashboardLayout title="Timetable">
      <div className="flex items-center justify-between mb-6 flex-wrap gap-3">
        <h2 className="text-xl font-semibold text-gray-800">Weekly Timetable</h2>
        <select className="input w-40" value={selected} onChange={e => setSelected(e.target.value)}>
          <option value="">All Semesters</option>
          {semesters.map(s => <option key={s} value={s}>{s}</option>)}
        </select>
      </div>

      {loading ? (
        <div className="flex justify-center py-12"><div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600" /></div>
      ) : filtered.length === 0 ? (
        <div className="card text-center py-12">
          <Clock className="w-10 h-10 text-gray-300 mx-auto mb-3" />
          <p className="text-gray-400">No timetable entries found</p>
        </div>
      ) : (
        <div className="space-y-4">
          {DAYS.map(day => byDay[day].length > 0 && (
            <div key={day} className="card">
              <h3 className="font-semibold text-gray-700 mb-3 capitalize text-sm uppercase tracking-wide">{day}</h3>
              <div className="grid gap-2 sm:grid-cols-2 lg:grid-cols-3">
                {byDay[day].map(entry => (
                  <div key={entry.id} className="flex gap-3 p-3 bg-gray-50 rounded-lg border-l-4 border-primary-400">
                    <div className="flex-1">
                      <p className="text-sm font-semibold text-gray-800">{entry.courseName ?? entry.courseCode}</p>
                      <p className="text-xs text-gray-500">{entry.courseCode}</p>
                      {entry.facultyName && <p className="text-xs text-gray-400 mt-0.5">{entry.facultyName}</p>}
                    </div>
                    <div className="text-right text-xs text-gray-500">
                      <p className="font-medium text-gray-700">{entry.timeSlot}</p>
                      {entry.roomNumber && <p className="mt-0.5">Room {entry.roomNumber}</p>}
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

