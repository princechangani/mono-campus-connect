"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import api from "@/lib/api";
import toast from "react-hot-toast";
import { ClipboardCheck, Search, ChevronDown, ChevronUp, Users, CheckCircle2, XCircle, Clock } from "lucide-react";
import { formatDate } from "@/lib/utils";

const STATUS_STYLES: Record<string, string> = {
  PRESENT: "bg-green-100 text-green-700",
  ABSENT:  "bg-red-100  text-red-700",
  LATE:    "bg-amber-100 text-amber-700",
};

export default function FacultyAttendancePage() {
  const [courses,       setCourses]       = useState<any[]>([]);
  const [selCourse,     setSelCourse]     = useState("");
  const [date,          setDate]          = useState(new Date().toISOString().slice(0, 10));
  const [students,      setStudents]      = useState<any[]>([]);
  const [attendance,    setAttendance]    = useState<Record<number, string>>({});
  const [records,       setRecords]       = useState<any[]>([]);
  const [loading,       setLoading]       = useState(false);
  const [saving,        setSaving]        = useState(false);
  const [activeTab,     setActiveTab]     = useState<"mark" | "view">("mark");
  const [searchStudent, setSearchStudent] = useState("");

  // Load courses
  useEffect(() => {
    api.get("/courses").then(r => setCourses(r.data)).catch(() => {});
  }, []);

  // Load students in course when course changes
  useEffect(() => {
    if (!selCourse) return;
    setLoading(true);
    // Load existing attendance for selected date
    api.get(`/attendance/course/${selCourse}`)
      .then(r => {
        const today = r.data.filter((a: any) =>
          new Date(a.sessionDate).toISOString().slice(0, 10) === date
        );
        const map: Record<number, string> = {};
        today.forEach((a: any) => { map[a.studentId] = a.status; });
        setAttendance(map);
        setRecords(r.data);
      })
      .catch(() => {})
      .finally(() => {
        // load students from user list filtered by semester of course
        api.get("/admin/students")
          .then(r => setStudents(r.data))
          .catch(() => {})
          .finally(() => setLoading(false));
      });
  }, [selCourse, date]);

  const setStatus = (studentId: number, status: string) =>
    setAttendance(prev => ({ ...prev, [studentId]: status }));

  const markAll = (status: string) => {
    const all: Record<number, string> = {};
    students.forEach(s => { all[s.id] = status; });
    setAttendance(all);
  };

  const handleBulkSubmit = async () => {
    if (!selCourse) return toast.error("Select a course first");
    const unmarked = students.filter(s => !attendance[s.id]);
    if (unmarked.length > 0) return toast.error(`Mark status for ${unmarked.length} student(s) first`);

    setSaving(true);
    try {
      const requests = students.map(s => ({
        courseCode:  selCourse,
        studentId:   s.id,
        sessionDate: date,
        status:      attendance[s.id],
      }));
      await api.post("/attendance/bulk", requests);
      toast.success(`Attendance saved for ${students.length} students`);
    } catch (e: any) {
      toast.error(e?.response?.data?.message ?? "Failed to save attendance");
    } finally {
      setSaving(false);
    }
  };

  const filteredStudents = students.filter(s =>
    `${s.firstName} ${s.lastName} ${s.enrollmentNumber ?? ""}`.toLowerCase()
      .includes(searchStudent.toLowerCase())
  );

  // Summary counts
  const present = Object.values(attendance).filter(v => v === "PRESENT").length;
  const absent  = Object.values(attendance).filter(v => v === "ABSENT").length;
  const late    = Object.values(attendance).filter(v => v === "LATE").length;

  return (
    <DashboardLayout title="Attendance">
      {/* Tabs */}
      <div className="flex gap-1 mb-6 bg-gray-100 p-1 rounded-lg w-fit">
        {(["mark", "view"] as const).map(tab => (
          <button key={tab} onClick={() => setActiveTab(tab)}
            className={`px-4 py-1.5 rounded-md text-sm font-medium transition-colors ${
              activeTab === tab ? "bg-white text-primary-700 shadow-sm" : "text-gray-500 hover:text-gray-700"
            }`}>
            {tab === "mark" ? "Mark Attendance" : "View Records"}
          </button>
        ))}
      </div>

      {activeTab === "mark" && (
        <>
          {/* Controls */}
          <div className="card mb-5">
            <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
              <div>
                <label className="block text-xs font-medium text-gray-600 mb-1">Course / Subject *</label>
                <select className="input" value={selCourse} onChange={e => setSelCourse(e.target.value)}>
                  <option value="">Select course…</option>
                  {courses.map(c => (
                    <option key={c.id} value={c.courseCode}>{c.courseCode} – {c.courseName}</option>
                  ))}
                </select>
              </div>
              <div>
                <label className="block text-xs font-medium text-gray-600 mb-1">Date *</label>
                <input type="date" className="input" value={date} onChange={e => setDate(e.target.value)} />
              </div>
              <div>
                <label className="block text-xs font-medium text-gray-600 mb-1">Search Student</label>
                <div className="relative">
                  <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                  <input className="input pl-9" placeholder="Name or enrollment…"
                    value={searchStudent} onChange={e => setSearchStudent(e.target.value)} />
                </div>
              </div>
            </div>
          </div>

          {selCourse && (
            <>
              {/* Summary + bulk actions */}
              <div className="flex items-center justify-between mb-4 flex-wrap gap-3">
                <div className="flex gap-3">
                  <div className="flex items-center gap-1.5 text-sm text-green-700 bg-green-50 px-3 py-1.5 rounded-lg">
                    <CheckCircle2 className="w-4 h-4" /> {present} Present
                  </div>
                  <div className="flex items-center gap-1.5 text-sm text-red-700 bg-red-50 px-3 py-1.5 rounded-lg">
                    <XCircle className="w-4 h-4" /> {absent} Absent
                  </div>
                  <div className="flex items-center gap-1.5 text-sm text-amber-700 bg-amber-50 px-3 py-1.5 rounded-lg">
                    <Clock className="w-4 h-4" /> {late} Late
                  </div>
                </div>
                <div className="flex gap-2">
                  <button onClick={() => markAll("PRESENT")} className="text-xs px-3 py-1.5 bg-green-100 text-green-700 rounded-lg hover:bg-green-200">All Present</button>
                  <button onClick={() => markAll("ABSENT")}  className="text-xs px-3 py-1.5 bg-red-100  text-red-700  rounded-lg hover:bg-red-200">All Absent</button>
                </div>
              </div>

              {loading ? (
                <div className="flex justify-center py-10">
                  <div className="animate-spin rounded-full h-7 w-7 border-b-2 border-primary-600" />
                </div>
              ) : filteredStudents.length === 0 ? (
                <div className="card text-center py-10">
                  <Users className="w-8 h-8 text-gray-300 mx-auto mb-2" />
                  <p className="text-gray-400 text-sm">No students found</p>
                </div>
              ) : (
                <>
                  <div className="card !p-0 overflow-hidden mb-4">
                    <table className="w-full text-sm">
                      <thead className="bg-gray-50 text-xs uppercase text-gray-500">
                        <tr>
                          <th className="px-4 py-3 text-left">#</th>
                          <th className="px-4 py-3 text-left">Student</th>
                          <th className="px-4 py-3 text-left">Enrollment</th>
                          <th className="px-4 py-3 text-center">Status</th>
                        </tr>
                      </thead>
                      <tbody className="divide-y divide-gray-100">
                        {filteredStudents.map((s, i) => (
                          <tr key={s.id} className="hover:bg-gray-50">
                            <td className="px-4 py-3 text-gray-400">{i + 1}</td>
                            <td className="px-4 py-3">
                              <div className="flex items-center gap-2">
                                <div className="w-7 h-7 rounded-full bg-primary-100 text-primary-700 flex items-center justify-center text-xs font-bold">
                                  {s.firstName?.[0]}{s.lastName?.[0]}
                                </div>
                                <span className="font-medium text-gray-800">{s.firstName} {s.lastName}</span>
                              </div>
                            </td>
                            <td className="px-4 py-3 text-gray-500">{s.enrollmentNumber ?? "—"}</td>
                            <td className="px-4 py-3">
                              <div className="flex justify-center gap-2">
                                {["PRESENT", "ABSENT", "LATE"].map(st => (
                                  <button key={st} onClick={() => setStatus(s.id, st)}
                                    className={`px-2.5 py-1 rounded-md text-xs font-medium border transition-colors ${
                                      attendance[s.id] === st
                                        ? STATUS_STYLES[st] + " border-transparent"
                                        : "text-gray-400 border-gray-200 hover:border-gray-300"
                                    }`}>
                                    {st[0] + st.slice(1).toLowerCase()}
                                  </button>
                                ))}
                              </div>
                            </td>
                          </tr>
                        ))}
                      </tbody>
                    </table>
                  </div>
                  <div className="flex justify-end">
                    <button onClick={handleBulkSubmit} disabled={saving} className="btn-primary">
                      {saving ? "Saving…" : `Submit Attendance (${students.length} students)`}
                    </button>
                  </div>
                </>
              )}
            </>
          )}
        </>
      )}

      {activeTab === "view" && (
        <>
          <div className="flex gap-3 mb-5">
            <select className="input w-64" value={selCourse} onChange={e => setSelCourse(e.target.value)}>
              <option value="">Select course…</option>
              {courses.map(c => (
                <option key={c.id} value={c.courseCode}>{c.courseCode} – {c.courseName}</option>
              ))}
            </select>
          </div>
          {records.length === 0 ? (
            <div className="card text-center py-12">
              <ClipboardCheck className="w-10 h-10 text-gray-300 mx-auto mb-3" />
              <p className="text-gray-400">Select a course to view records</p>
            </div>
          ) : (
            <div className="card !p-0 overflow-hidden">
              <table className="w-full text-sm">
                <thead className="bg-gray-50 text-xs uppercase text-gray-500">
                  <tr>
                    <th className="px-4 py-3 text-left">Student ID</th>
                    <th className="px-4 py-3 text-left">Date</th>
                    <th className="px-4 py-3 text-left">Status</th>
                    <th className="px-4 py-3 text-left">Remarks</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-100">
                  {records.slice(0, 100).map(r => (
                    <tr key={r.id} className="hover:bg-gray-50">
                      <td className="px-4 py-2.5 font-mono text-gray-600">{r.studentId}</td>
                      <td className="px-4 py-2.5 text-gray-600">{formatDate(r.sessionDate)}</td>
                      <td className="px-4 py-2.5">
                        <span className={`badge text-xs ${STATUS_STYLES[r.status] ?? "bg-gray-100 text-gray-600"}`}>
                          {r.status}
                        </span>
                      </td>
                      <td className="px-4 py-2.5 text-gray-400 text-xs">{r.remarks ?? "—"}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </>
      )}
    </DashboardLayout>
  );
}

