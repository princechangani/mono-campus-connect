"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import api from "@/lib/api";
import toast from "react-hot-toast";
import { Plus, X, BarChart2, Search, Pencil } from "lucide-react";
import { useForm } from "react-hook-form";

const GRADE_BADGE: Record<string, string> = {
  "A+": "bg-green-100 text-green-800",
  "A":  "bg-green-100 text-green-700",
  "B+": "bg-blue-100 text-blue-700",
  "B":  "bg-blue-50 text-blue-600",
  "C":  "bg-yellow-100 text-yellow-700",
  "D":  "bg-orange-100 text-orange-700",
  "F":  "bg-red-100 text-red-700",
};

export default function FacultyResultsPage() {
  const [results,   setResults]   = useState<any[]>([]);
  const [exams,     setExams]     = useState<any[]>([]);
  const [search,    setSearch]    = useState("");
  const [selExam,   setSelExam]   = useState("");
  const [loading,   setLoading]   = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editing,   setEditing]   = useState<any>(null);

  const { register, handleSubmit, reset, watch, formState: { isSubmitting } } = useForm<any>();
  const watchObtained = watch("obtainedMarks");
  const watchTotal    = watch("totalMarks");

  const load = () => {
    api.get("/results").then(r => setResults(r.data)).catch(() => {}).finally(() => setLoading(false));
  };
  useEffect(() => {
    load();
    api.get("/exams").then(r => setExams(r.data)).catch(() => {});
  }, []);

  const openCreate = () => { setEditing(null); reset({ status: "PENDING" }); setShowModal(true); };
  const openEdit   = (r: any) => { setEditing(r); reset(r); setShowModal(true); };

  // Auto-calculate grade from marks
  const calcGrade = (obtained: number, total: number): string => {
    if (!total) return "";
    const pct = (obtained / total) * 100;
    if (pct >= 90) return "A+";
    if (pct >= 80) return "A";
    if (pct >= 70) return "B+";
    if (pct >= 60) return "B";
    if (pct >= 50) return "C";
    if (pct >= 40) return "D";
    return "F";
  };

  const onSubmit = async (data: any) => {
    try {
      const payload = {
        ...data,
        studentId:    Number(data.studentId),
        totalMarks:   Number(data.totalMarks),
        obtainedMarks: Number(data.obtainedMarks),
        grade:        calcGrade(Number(data.obtainedMarks), Number(data.totalMarks)),
      };
      if (editing) {
        await api.put(`/results/${editing.id}`, payload);
        toast.success("Result updated");
      } else {
        await api.post("/results", payload);
        toast.success("Result added");
      }
      setShowModal(false); load();
    } catch { toast.error("Operation failed"); }
  };

  const publishResult = async (id: number) => {
    try {
      await api.put(`/results/${id}/status`, { status: "PUBLISHED" });
      toast.success("Result published");
      load();
    } catch { toast.error("Failed"); }
  };

  const filtered = results.filter(r => {
    const matchSearch = `${r.studentId} ${r.examCode} ${r.courseCode}`.toLowerCase().includes(search.toLowerCase());
    const matchExam   = selExam ? r.examCode === selExam : true;
    return matchSearch && matchExam;
  });

  // stats
  const total     = filtered.length;
  const published = filtered.filter(r => r.status === "PUBLISHED").length;
  const avgObtained = total > 0 ? (filtered.reduce((s, r) => s + (r.obtainedMarks || 0), 0) / total).toFixed(1) : "—";

  return (
    <DashboardLayout title="Results">
      <div className="flex items-center justify-between mb-6 flex-wrap gap-3">
        <h2 className="text-xl font-semibold text-gray-800">Results</h2>
        <div className="flex gap-2 flex-wrap">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
            <input className="input pl-9 w-44" placeholder="Search…" value={search} onChange={e => setSearch(e.target.value)} />
          </div>
          <select className="input w-52" value={selExam} onChange={e => setSelExam(e.target.value)}>
            <option value="">All Exams</option>
            {exams.map(e => <option key={e.id} value={e.examCode}>{e.examCode}</option>)}
          </select>
          <button onClick={openCreate} className="btn-primary flex items-center gap-2">
            <Plus className="w-4 h-4" /> Add Result
          </button>
        </div>
      </div>

      {/* Summary cards */}
      <div className="grid grid-cols-3 gap-4 mb-6">
        <div className="card text-center !py-4">
          <p className="text-2xl font-bold text-primary-600">{total}</p>
          <p className="text-xs text-gray-500 mt-1">Total Results</p>
        </div>
        <div className="card text-center !py-4">
          <p className="text-2xl font-bold text-green-600">{published}</p>
          <p className="text-xs text-gray-500 mt-1">Published</p>
        </div>
        <div className="card text-center !py-4">
          <p className="text-2xl font-bold text-blue-600">{avgObtained}</p>
          <p className="text-xs text-gray-500 mt-1">Avg. Marks</p>
        </div>
      </div>

      {loading ? (
        <div className="flex justify-center py-12"><div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600" /></div>
      ) : filtered.length === 0 ? (
        <div className="card text-center py-14">
          <BarChart2 className="w-10 h-10 text-gray-300 mx-auto mb-3" />
          <p className="text-gray-400">No results found</p>
        </div>
      ) : (
        <div className="card !p-0 overflow-hidden">
          <table className="w-full text-sm">
            <thead className="bg-gray-50 text-xs uppercase text-gray-500">
              <tr>
                <th className="px-4 py-3 text-left">Student ID</th>
                <th className="px-4 py-3 text-left">Exam</th>
                <th className="px-4 py-3 text-left">Course</th>
                <th className="px-4 py-3 text-center">Marks</th>
                <th className="px-4 py-3 text-center">Grade</th>
                <th className="px-4 py-3 text-center">Status</th>
                <th className="px-4 py-3 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-100">
              {filtered.map(r => (
                <tr key={r.id} className="hover:bg-gray-50">
                  <td className="px-4 py-3 font-mono text-gray-600">{r.studentId}</td>
                  <td className="px-4 py-3 text-gray-700">{r.examCode}</td>
                  <td className="px-4 py-3 text-gray-500">{r.courseCode}</td>
                  <td className="px-4 py-3 text-center text-gray-700">{r.obtainedMarks}/{r.totalMarks}</td>
                  <td className="px-4 py-3 text-center">
                    <span className={`badge text-xs ${GRADE_BADGE[r.grade] ?? "bg-gray-100 text-gray-600"}`}>{r.grade ?? "—"}</span>
                  </td>
                  <td className="px-4 py-3 text-center">
                    <span className={`badge text-xs ${r.status === "PUBLISHED" ? "bg-green-100 text-green-700" : "bg-gray-100 text-gray-500"}`}>
                      {r.status ?? "PENDING"}
                    </span>
                  </td>
                  <td className="px-4 py-3 text-right">
                    <div className="flex gap-1 justify-end">
                      <button onClick={() => openEdit(r)} className="p-1.5 hover:bg-gray-100 rounded text-gray-400"><Pencil className="w-3.5 h-3.5" /></button>
                      {r.status !== "PUBLISHED" && (
                        <button onClick={() => publishResult(r.id)}
                          className="px-2 py-1 text-xs bg-green-100 text-green-700 rounded-md hover:bg-green-200">
                          Publish
                        </button>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl shadow-xl w-full max-w-md max-h-[92vh] overflow-y-auto">
            <div className="flex items-center justify-between p-5 border-b sticky top-0 bg-white">
              <h3 className="font-semibold">{editing ? "Edit Result" : "Add Result"}</h3>
              <button onClick={() => setShowModal(false)}><X className="w-5 h-5 text-gray-400" /></button>
            </div>
            <form onSubmit={handleSubmit(onSubmit)} className="p-5 space-y-3">
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Student ID *</label>
                  <input type="number" {...register("studentId", { required: true })} className="input" />
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Exam *</label>
                  <select {...register("examId", { required: true })} className="input">
                    <option value="">Select…</option>
                    {exams.map(e => <option key={e.id} value={e.id}>{e.examCode}</option>)}
                  </select>
                </div>
              </div>
              <div>
                <label className="block text-xs font-medium text-gray-700 mb-1">Course Code</label>
                <input {...register("courseCode")} className="input" />
              </div>
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Total Marks *</label>
                  <input type="number" {...register("totalMarks", { required: true })} className="input" />
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Obtained Marks *</label>
                  <input type="number" {...register("obtainedMarks", { required: true })} className="input" />
                </div>
              </div>
              {watchObtained && watchTotal && (
                <p className="text-xs text-gray-500">
                  Auto grade: <strong>{calcGrade(Number(watchObtained), Number(watchTotal))}</strong>
                  {" "}({((Number(watchObtained)/Number(watchTotal))*100).toFixed(1)}%)
                </p>
              )}
              <div>
                <label className="block text-xs font-medium text-gray-700 mb-1">Status</label>
                <select {...register("status")} className="input">
                  <option value="PENDING">Pending</option>
                  <option value="PUBLISHED">Published</option>
                </select>
              </div>
              <div>
                <label className="block text-xs font-medium text-gray-700 mb-1">Comments</label>
                <textarea {...register("comments")} className="input resize-none" rows={2} />
              </div>
              <div className="flex justify-end gap-2 pt-2">
                <button type="button" onClick={() => setShowModal(false)} className="btn-secondary text-sm">Cancel</button>
                <button type="submit" disabled={isSubmitting} className="btn-primary text-sm">
                  {isSubmitting ? "Saving…" : editing ? "Update" : "Add"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </DashboardLayout>
  );
}

