"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import api from "@/lib/api";
import toast from "react-hot-toast";
import { Plus, Pencil, Trash2, X, Clock } from "lucide-react";
import { useForm } from "react-hook-form";

const DAYS = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"];

const DAY_COLORS: Record<string, string> = {
  MONDAY: "bg-blue-50 border-blue-200",
  TUESDAY: "bg-green-50 border-green-200",
  WEDNESDAY: "bg-yellow-50 border-yellow-200",
  THURSDAY: "bg-purple-50 border-purple-200",
  FRIDAY: "bg-pink-50 border-pink-200",
  SATURDAY: "bg-orange-50 border-orange-200",
};

export default function AdminTimetablePage() {
  const [entries,   setEntries]   = useState<any[]>([]);
  const [courses,   setCourses]   = useState<any[]>([]);
  const [semesters, setSemesters] = useState<string[]>([]);
  const [selSem,    setSelSem]    = useState("");
  const [loading,   setLoading]   = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editing,   setEditing]   = useState<any>(null);

  const { register, handleSubmit, reset, watch, formState: { isSubmitting } } = useForm<any>();
  const watchCourse = watch("courseCode");

  const load = () => {
    api.get("/timetable").then(r => {
      setEntries(r.data);
      const uniq = [...new Set<string>(r.data.map((e: any) => e.semester))].sort();
      setSemesters(uniq);
      if (!selSem && uniq.length > 0) setSelSem(uniq[0]);
    }).catch(() => {}).finally(() => setLoading(false));
  };

  useEffect(() => { load(); }, []);
  useEffect(() => {
    api.get("/courses").then(r => setCourses(r.data)).catch(() => {});
  }, []);

  // auto-fill courseName and facultyName when courseCode changes
  useEffect(() => {
    if (!watchCourse) return;
    const found = courses.find(c => c.courseCode === watchCourse);
    if (found) {
      reset((prev: any) => ({ ...prev, courseName: found.courseName, facultyName: found.instructor ?? "" }));
    }
  }, [watchCourse, courses, reset]);

  const openCreate = () => { setEditing(null); reset({ semester: selSem }); setShowModal(true); };
  const openEdit   = (e: any) => { setEditing(e); reset(e); setShowModal(true); };

  const onSubmit = async (data: any) => {
    try {
      if (editing) {
        await api.put(`/timetable/${editing.id}`, data);
        toast.success("Entry updated");
      } else {
        await api.post("/timetable", data);
        toast.success("Entry added");
      }
      setShowModal(false); load();
    } catch { toast.error("Operation failed"); }
  };

  const deleteEntry = (id: number) => {
    if (!confirm("Delete this timetable entry?")) return;
    api.delete(`/timetable/${id}`).then(() => { toast.success("Deleted"); load(); }).catch(() => toast.error("Delete failed"));
  };

  const filtered = selSem ? entries.filter(e => e.semester === selSem) : entries;
  const byDay    = DAYS.reduce<Record<string, any[]>>((acc, d) => {
    acc[d] = filtered.filter(e => e.dayOfWeek === d).sort((a, b) => a.timeSlot.localeCompare(b.timeSlot));
    return acc;
  }, {});

  return (
    <DashboardLayout title="Timetable">
      <div className="flex items-center justify-between mb-6 flex-wrap gap-3">
        <h2 className="text-xl font-semibold text-gray-800">Timetable Management</h2>
        <div className="flex gap-2 flex-wrap">
          <select className="input w-36" value={selSem} onChange={e => setSelSem(e.target.value)}>
            <option value="">All Semesters</option>
            {semesters.map(s => <option key={s} value={s}>Sem {s}</option>)}
          </select>
          <button onClick={openCreate} className="btn-primary flex items-center gap-2">
            <Plus className="w-4 h-4" /> Add Entry
          </button>
        </div>
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
            <div key={day} className={`rounded-xl border p-4 ${DAY_COLORS[day]}`}>
              <h3 className="font-semibold text-gray-700 mb-3 text-sm uppercase tracking-wide">{day}</h3>
              <div className="grid gap-2 sm:grid-cols-2 lg:grid-cols-3">
                {byDay[day].map(entry => (
                  <div key={entry.id} className="bg-white rounded-lg p-3 border border-white shadow-sm flex gap-3">
                    <div className="flex-1 min-w-0">
                      <p className="text-sm font-semibold text-gray-800 truncate">{entry.courseName ?? entry.courseCode}</p>
                      <p className="text-xs text-gray-500">{entry.courseCode}</p>
                      <p className="text-xs text-gray-400 mt-0.5">{entry.facultyName}</p>
                    </div>
                    <div className="text-right">
                      <p className="text-xs font-medium text-gray-700">{entry.timeSlot}</p>
                      {entry.roomNumber && <p className="text-xs text-gray-400 mt-0.5">Room {entry.roomNumber}</p>}
                      <div className="flex gap-1 mt-1 justify-end">
                        <button onClick={() => openEdit(entry)} className="p-1 hover:bg-gray-100 rounded text-gray-400"><Pencil className="w-3 h-3" /></button>
                        <button onClick={() => deleteEntry(entry.id)} className="p-1 hover:bg-red-50 rounded text-red-400"><Trash2 className="w-3 h-3" /></button>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      )}

      {showModal && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl shadow-xl w-full max-w-lg max-h-[90vh] overflow-y-auto">
            <div className="flex items-center justify-between p-5 border-b sticky top-0 bg-white">
              <h3 className="font-semibold text-gray-800">{editing ? "Edit Entry" : "Add Timetable Entry"}</h3>
              <button onClick={() => setShowModal(false)}><X className="w-5 h-5 text-gray-400" /></button>
            </div>
            <form onSubmit={handleSubmit(onSubmit)} className="p-5 space-y-3">
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Day *</label>
                  <select {...register("dayOfWeek", { required: true })} className="input">
                    {DAYS.map(d => <option key={d} value={d}>{d}</option>)}
                  </select>
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Time Slot *</label>
                  <input {...register("timeSlot", { required: true })} className="input" placeholder="09:00-10:00" />
                </div>
              </div>
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Course Code *</label>
                  <select {...register("courseCode", { required: true })} className="input">
                    <option value="">Select course</option>
                    {courses.map(c => <option key={c.id} value={c.courseCode}>{c.courseCode} – {c.courseName}</option>)}
                  </select>
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Course Name</label>
                  <input {...register("courseName")} className="input" />
                </div>
              </div>
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Faculty Name</label>
                  <input {...register("facultyName")} className="input" />
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Faculty ID</label>
                  <input {...register("facultyId")} className="input" placeholder="FAC001" />
                </div>
              </div>
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Room Number</label>
                  <input {...register("roomNumber")} className="input" placeholder="A101" />
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Semester *</label>
                  <input {...register("semester", { required: true })} className="input" placeholder="4" />
                </div>
              </div>
              <div className="flex justify-end gap-2 pt-2">
                <button type="button" onClick={() => setShowModal(false)} className="btn-secondary text-sm">Cancel</button>
                <button type="submit" disabled={isSubmitting} className="btn-primary text-sm">
                  {isSubmitting ? "Saving..." : editing ? "Update" : "Add"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </DashboardLayout>
  );
}

