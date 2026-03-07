"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import api from "@/lib/api";
import toast from "react-hot-toast";
import { Plus, Trash2, X, BookOpen, Download, Search, Eye } from "lucide-react";
import { formatDate } from "@/lib/utils";
import { getUser } from "@/lib/auth";

const TYPE_STYLES: Record<string, string> = {
  PDF:   "bg-red-100   text-red-700",
  DOC:   "bg-blue-100  text-blue-700",
  PPT:   "bg-orange-100 text-orange-700",
  VIDEO: "bg-purple-100 text-purple-700",
  LINK:  "bg-green-100  text-green-700",
};

const MATERIAL_TYPES = ["PDF", "DOC", "PPT", "VIDEO", "LINK"];

export default function FacultyMaterialsPage() {
  const [materials,  setMaterials]  = useState<any[]>([]);
  const [courses,    setCourses]    = useState<any[]>([]);
  const [search,     setSearch]     = useState("");
  const [selCourse,  setSelCourse]  = useState("");
  const [loading,    setLoading]    = useState(true);
  const [showModal,  setShowModal]  = useState(false);
  const [uploading,  setUploading]  = useState(false);

  const [form, setForm] = useState({
    materialCode: "", courseCode: "", title: "",
    description: "", type: "PDF", uploadedBy: "",
  });
  const [file, setFile] = useState<File | null>(null);

  const user = getUser();

  const load = () => {
    api.get("/materials").then(r => setMaterials(r.data)).catch(() => {}).finally(() => setLoading(false));
  };
  useEffect(() => {
    load();
    api.get("/courses").then(r => setCourses(r.data)).catch(() => {});
  }, []);

  const openModal = () => {
    setForm({ materialCode: "", courseCode: "", title: "", description: "", type: "PDF",
      uploadedBy: user ? `${user.firstName} ${user.lastName}` : "" });
    setFile(null);
    setShowModal(true);
  };

  const handleUpload = async () => {
    if (!form.materialCode || !form.courseCode || !form.title || !file)
      return toast.error("Fill all required fields and select a file");

    setUploading(true);
    const fd = new FormData();
    fd.append("materialCode", form.materialCode);
    fd.append("courseCode",   form.courseCode);
    fd.append("title",        form.title);
    fd.append("description",  form.description);
    fd.append("type",         form.type);
    fd.append("uploadedBy",   form.uploadedBy);
    fd.append("file",         file);

    try {
      await api.post("/materials", fd, { headers: { "Content-Type": "multipart/form-data" } });
      toast.success("Material uploaded");
      setShowModal(false); load();
    } catch { toast.error("Upload failed"); }
    finally { setUploading(false); }
  };

  const deleteMaterial = (id: number) => {
    if (!confirm("Delete this material?")) return;
    api.delete(`/materials/${id}`)
      .then(() => { toast.success("Deleted"); load(); })
      .catch(() => toast.error("Delete failed"));
  };

  const filtered = materials.filter(m => {
    const matchSearch = `${m.title} ${m.courseCode} ${m.uploadedBy}`.toLowerCase().includes(search.toLowerCase());
    const matchCourse = selCourse ? m.courseCode === selCourse : true;
    return matchSearch && matchCourse;
  });

  return (
    <DashboardLayout title="Materials">
      <div className="flex items-center justify-between mb-6 flex-wrap gap-3">
        <h2 className="text-xl font-semibold text-gray-800">Study Materials</h2>
        <div className="flex gap-2 flex-wrap">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
            <input className="input pl-9 w-44" placeholder="Search…" value={search} onChange={e => setSearch(e.target.value)} />
          </div>
          <select className="input w-48" value={selCourse} onChange={e => setSelCourse(e.target.value)}>
            <option value="">All Courses</option>
            {courses.map(c => <option key={c.id} value={c.courseCode}>{c.courseCode}</option>)}
          </select>
          <button onClick={openModal} className="btn-primary flex items-center gap-2">
            <Plus className="w-4 h-4" /> Upload Material
          </button>
        </div>
      </div>

      {loading ? (
        <div className="flex justify-center py-16">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600" />
        </div>
      ) : filtered.length === 0 ? (
        <div className="card text-center py-14">
          <BookOpen className="w-10 h-10 text-gray-300 mx-auto mb-3" />
          <p className="text-gray-400">No materials found</p>
        </div>
      ) : (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {filtered.map(m => (
            <div key={m.id} className="card hover:shadow-md transition-shadow !p-0 overflow-hidden">
              <div className="h-1 bg-gradient-to-r from-primary-400 to-primary-600" />
              <div className="p-4">
                <div className="flex items-start justify-between gap-2 mb-2">
                  <div className="flex gap-1 flex-wrap">
                    <span className={`badge text-xs ${TYPE_STYLES[m.type] ?? "bg-gray-100 text-gray-600"}`}>{m.type}</span>
                    <span className="badge bg-gray-100 text-gray-600 text-xs font-mono">{m.courseCode}</span>
                  </div>
                  <button onClick={() => deleteMaterial(m.id)} className="p-1.5 hover:bg-red-50 rounded text-red-400 flex-shrink-0">
                    <Trash2 className="w-3.5 h-3.5" />
                  </button>
                </div>
                <h3 className="font-semibold text-gray-800 leading-snug">{m.title}</h3>
                {m.description && <p className="text-xs text-gray-400 mt-1 line-clamp-2">{m.description}</p>}
                <div className="flex items-center justify-between mt-3 pt-3 border-t border-gray-100 text-xs text-gray-400">
                  <div>
                    <p>By <span className="text-gray-600">{m.uploadedBy}</span></p>
                    <p className="mt-0.5">{formatDate(m.uploadedDate)}</p>
                  </div>
                  <div className="flex items-center gap-1 text-gray-400">
                    <Eye className="w-3.5 h-3.5" />
                    <span>{m.downloadCount ?? 0}</span>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Upload Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl shadow-xl w-full max-w-lg max-h-[92vh] overflow-y-auto">
            <div className="flex items-center justify-between p-5 border-b sticky top-0 bg-white">
              <h3 className="font-semibold text-gray-800">Upload Material</h3>
              <button onClick={() => setShowModal(false)}><X className="w-5 h-5 text-gray-400" /></button>
            </div>
            <div className="p-5 space-y-3">
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Material Code *</label>
                  <input className="input" value={form.materialCode}
                    onChange={e => setForm(p => ({ ...p, materialCode: e.target.value }))} placeholder="MAT-CS401-01" />
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Type *</label>
                  <select className="input" value={form.type}
                    onChange={e => setForm(p => ({ ...p, type: e.target.value }))}>
                    {MATERIAL_TYPES.map(t => <option key={t} value={t}>{t}</option>)}
                  </select>
                </div>
              </div>
              <div>
                <label className="block text-xs font-medium text-gray-700 mb-1">Course *</label>
                <select className="input" value={form.courseCode}
                  onChange={e => setForm(p => ({ ...p, courseCode: e.target.value }))}>
                  <option value="">Select course…</option>
                  {courses.map(c => <option key={c.id} value={c.courseCode}>{c.courseCode} – {c.courseName}</option>)}
                </select>
              </div>
              <div>
                <label className="block text-xs font-medium text-gray-700 mb-1">Title *</label>
                <input className="input" value={form.title}
                  onChange={e => setForm(p => ({ ...p, title: e.target.value }))} />
              </div>
              <div>
                <label className="block text-xs font-medium text-gray-700 mb-1">Description</label>
                <textarea className="input resize-none" rows={2} value={form.description}
                  onChange={e => setForm(p => ({ ...p, description: e.target.value }))} />
              </div>
              <div>
                <label className="block text-xs font-medium text-gray-700 mb-1">File *</label>
                <input type="file" className="input" onChange={e => setFile(e.target.files?.[0] ?? null)} />
              </div>
              <div className="flex justify-end gap-2 pt-2">
                <button onClick={() => setShowModal(false)} className="btn-secondary text-sm">Cancel</button>
                <button onClick={handleUpload} disabled={uploading} className="btn-primary text-sm">
                  {uploading ? "Uploading…" : "Upload"}
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </DashboardLayout>
  );
}

