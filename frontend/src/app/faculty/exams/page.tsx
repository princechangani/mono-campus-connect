"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import api from "@/lib/api";
import toast from "react-hot-toast";
import { Plus, Pencil, Trash2, X, FileText, Search, Users } from "lucide-react";
import { useForm } from "react-hook-form";
import { formatDate } from "@/lib/utils";

const EXAM_TYPES = ["MIDTERM", "FINAL", "QUIZ", "ASSIGNMENT"];

const TYPE_BADGE: Record<string, string> = {
  MIDTERM:    "bg-blue-100   text-blue-700",
  FINAL:      "bg-red-100    text-red-700",
  QUIZ:       "bg-green-100  text-green-700",
  ASSIGNMENT: "bg-amber-100  text-amber-700",
};

export default function FacultyExamsPage() {
  const [exams,     setExams]     = useState<any[]>([]);
  const [courses,   setCourses]   = useState<any[]>([]);
  const [search,    setSearch]    = useState("");
  const [loading,   setLoading]   = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editing,   setEditing]   = useState<any>(null);
  const [enrollModal, setEnrollModal] = useState<any>(null);
  const [enrollId,    setEnrollId]   = useState("");

  const { register, handleSubmit, reset, formState: { isSubmitting } } = useForm<any>();

  const load = () => {
    api.get("/exams").then(r => setExams(r.data)).catch(() => {}).finally(() => setLoading(false));
  };
  useEffect(() => {
    load();
    api.get("/courses").then(r => setCourses(r.data)).catch(() => {});
  }, []);

  const openCreate = () => { setEditing(null); reset({ type: "QUIZ" }); setShowModal(true); };
  const openEdit   = (e: any) => { setEditing(e); reset({
    ...e,
    startDate: e.startDate ? new Date(e.startDate).toISOString().slice(0,16) : "",
    endDate:   e.endDate   ? new Date(e.endDate).toISOString().slice(0,16)   : "",
  }); setShowModal(true); };

  const onSubmit = async (data: any) => {
    try {
      const payload = { ...data, startDate: new Date(data.startDate), endDate: new Date(data.endDate) };
      if (editing) {
        await api.put(`/exams/${editing.id}`, payload);
        toast.success("Exam updated");
      } else {
        await api.post("/exams", payload);
        toast.success("Exam created");
      }
      setShowModal(false); load();
    } catch { toast.error("Operation failed"); }
  };

  const deleteExam = (code: string) => {
    if (!confirm("Delete this exam?")) return;
    api.delete(`/exams/${code}`)
      .then(() => { toast.success("Deleted"); load(); })
      .catch(() => toast.error("Delete failed"));
  };

  const enrollStudent = async () => {
    if (!enrollId.trim()) return toast.error("Enter a student ID");
    try {
      await api.post(`/exams/${enrollModal.examCode}/enroll?studentId=${enrollId}`);
      toast.success("Student enrolled");
      setEnrollId(""); load();
    } catch { toast.error("Enroll failed"); }
  };

  const filtered = exams.filter(e =>
    `${e.examCode} ${e.title} ${e.courseCode}`.toLowerCase().includes(search.toLowerCase())
  );

  const isUpcoming = (e: any) => new Date(e.startDate) > new Date();
  const isPast     = (e: any) => new Date(e.endDate)   < new Date();

  return (
    <DashboardLayout title="Exams">
      <div className="flex items-center justify-between mb-6 flex-wrap gap-3">
        <h2 className="text-xl font-semibold text-gray-800">Exams Management</h2>
        <div className="flex gap-2">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
            <input className="input pl-9 w-52" placeholder="Search exams…" value={search} onChange={e => setSearch(e.target.value)} />
          </div>
          <button onClick={openCreate} className="btn-primary flex items-center gap-2">
            <Plus className="w-4 h-4" /> Create Exam
          </button>
        </div>
      </div>

      {loading ? (
        <div className="flex justify-center py-16"><div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600" /></div>
      ) : filtered.length === 0 ? (
        <div className="card text-center py-14">
          <FileText className="w-10 h-10 text-gray-300 mx-auto mb-3" />
          <p className="text-gray-400">No exams found</p>
        </div>
      ) : (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {filtered.map(e => (
            <div key={e.id} className="card hover:shadow-md transition-shadow !p-0 overflow-hidden">
              <div className={`h-1.5 ${isUpcoming(e) ? "bg-green-400" : isPast(e) ? "bg-gray-300" : "bg-primary-400"}`} />
              <div className="p-4">
                <div className="flex items-start justify-between gap-2 mb-2">
                  <div className="flex flex-wrap gap-1">
                    <span className={`badge text-xs ${TYPE_BADGE[e.type] ?? "bg-gray-100 text-gray-600"}`}>{e.type}</span>
                    <span className="badge bg-gray-100 text-gray-600 text-xs font-mono">{e.examCode}</span>
                    {isUpcoming(e) && <span className="badge bg-green-50 text-green-700 text-xs">Upcoming</span>}
                    {isPast(e)     && <span className="badge bg-gray-100 text-gray-400 text-xs">Past</span>}
                  </div>
                  <div className="flex gap-1 flex-shrink-0">
                    <button onClick={() => openEdit(e)} className="p-1.5 hover:bg-gray-100 rounded text-gray-400"><Pencil className="w-3.5 h-3.5" /></button>
                    <button onClick={() => deleteExam(e.examCode)} className="p-1.5 hover:bg-red-50 rounded text-red-400"><Trash2 className="w-3.5 h-3.5" /></button>
                  </div>
                </div>
                <h3 className="font-semibold text-gray-800">{e.title}</h3>
                <p className="text-xs text-gray-500 mt-0.5">{e.courseCode}</p>
                <p className="text-xs text-gray-400 mt-1 line-clamp-2">{e.description}</p>
                <div className="mt-3 pt-3 border-t border-gray-100 flex items-center justify-between text-xs text-gray-500">
                  <div>
                    <p>📅 {formatDate(e.startDate)}</p>
                    <p className="mt-0.5">⏰ {formatDate(e.endDate)}</p>
                  </div>
                  <button onClick={() => setEnrollModal(e)}
                    className="flex items-center gap-1 text-primary-600 hover:text-primary-800 text-xs font-medium">
                    <Users className="w-3.5 h-3.5" />
                    {e.enrolledStudents?.length ?? 0} students
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Create/Edit Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl shadow-xl w-full max-w-lg max-h-[92vh] overflow-y-auto">
            <div className="flex items-center justify-between p-5 border-b sticky top-0 bg-white">
              <h3 className="font-semibold text-gray-800">{editing ? "Edit Exam" : "Create Exam"}</h3>
              <button onClick={() => setShowModal(false)}><X className="w-5 h-5 text-gray-400" /></button>
            </div>
            <form onSubmit={handleSubmit(onSubmit)} className="p-5 space-y-3">
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Exam Code *</label>
                  <input {...register("examCode", { required: true })} className="input" placeholder="MID-CS401-2026" />
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Type *</label>
                  <select {...register("type", { required: true })} className="input">
                    {EXAM_TYPES.map(t => <option key={t} value={t}>{t}</option>)}
                  </select>
                </div>
              </div>
              <div>
                <label className="block text-xs font-medium text-gray-700 mb-1">Title *</label>
                <input {...register("title", { required: true })} className="input" placeholder="Mid-Term Examination" />
              </div>
              <div>
                <label className="block text-xs font-medium text-gray-700 mb-1">Course *</label>
                <select {...register("courseCode", { required: true })} className="input">
                  <option value="">Select course…</option>
                  {courses.map(c => <option key={c.id} value={c.courseCode}>{c.courseCode} – {c.courseName}</option>)}
                </select>
              </div>
              <div>
                <label className="block text-xs font-medium text-gray-700 mb-1">Description</label>
                <textarea {...register("description")} className="input resize-none" rows={2} />
              </div>
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Start Date *</label>
                  <input type="datetime-local" {...register("startDate", { required: true })} className="input" />
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">End Date *</label>
                  <input type="datetime-local" {...register("endDate", { required: true })} className="input" />
                </div>
              </div>
              <div className="flex justify-end gap-2 pt-2">
                <button type="button" onClick={() => setShowModal(false)} className="btn-secondary text-sm">Cancel</button>
                <button type="submit" disabled={isSubmitting} className="btn-primary text-sm">
                  {isSubmitting ? "Saving…" : editing ? "Update" : "Create"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Enroll Modal */}
      {enrollModal && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl shadow-xl w-full max-w-md">
            <div className="flex items-center justify-between p-5 border-b">
              <h3 className="font-semibold text-gray-800">Enrolled Students — {enrollModal.title}</h3>
              <button onClick={() => setEnrollModal(null)}><X className="w-5 h-5 text-gray-400" /></button>
            </div>
            <div className="p-5">
              <div className="flex gap-2 mb-4">
                <input className="input flex-1" placeholder="Student ID to enroll…"
                  value={enrollId} onChange={e => setEnrollId(e.target.value)} />
                <button onClick={enrollStudent} className="btn-primary text-sm">Enroll</button>
              </div>
              <div className="space-y-1 max-h-52 overflow-y-auto">
                {(enrollModal.enrolledStudents ?? []).length === 0 ? (
                  <p className="text-sm text-gray-400 text-center py-4">No students enrolled yet</p>
                ) : (enrollModal.enrolledStudents ?? []).map((sid: string) => (
                  <div key={sid} className="flex items-center justify-between px-3 py-2 bg-gray-50 rounded-lg text-sm">
                    <span className="font-mono text-gray-600">{sid}</span>
                    <button onClick={async () => {
                      await api.delete(`/exams/${enrollModal.examCode}/enroll?studentId=${sid}`);
                      toast.success("Removed"); load();
                      setEnrollModal((prev: any) => ({ ...prev, enrolledStudents: prev.enrolledStudents.filter((s: string) => s !== sid) }));
                    }} className="text-red-400 hover:text-red-600 text-xs">Remove</button>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      )}
    </DashboardLayout>
  );
}

