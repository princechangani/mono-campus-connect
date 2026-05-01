"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import api from "@/lib/api";
import toast from "react-hot-toast";
import { Plus, Pencil, Trash2, X, Search } from "lucide-react";
import { useForm } from "react-hook-form";

export default function AdminDepartmentsPage() {
  const [departments, setDepartments] = useState<any[]>([]);
  const [search, setSearch]           = useState("");
  const [loading, setLoading]         = useState(true);
  const [showModal, setShowModal]     = useState(false);
  const [editing, setEditing]         = useState<any>(null);

  const { register, handleSubmit, reset, formState: { isSubmitting } } = useForm<any>();

  const load = () => {
    api.get("/departments").then(r => setDepartments(r.data)).catch(() => {}).finally(() => setLoading(false));
  };
  useEffect(() => { load(); }, []);

  const openCreate = () => { setEditing(null); reset({}); setShowModal(true); };
  const openEdit   = (d: any) => { setEditing(d); reset(d); setShowModal(true); };

  const onSubmit = async (data: any) => {
    try {
      if (editing) {
        await api.put(`/departments/${editing.id}`, data);
        toast.success("Department updated");
      } else {
        await api.post("/departments", data);
        toast.success("Department created");
      }
      setShowModal(false); load();
    } catch { toast.error("Operation failed"); }
  };

  const deleteDept = (id: number) => {
    if (!confirm("Delete this department?")) return;
    api.delete(`/departments/${id}`).then(() => { toast.success("Deleted"); load(); }).catch(() => toast.error("Delete failed"));
  };

  const filtered = departments.filter(d =>
    `${d.name} ${d.code} ${d.headFacultyId ?? ""}`.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <DashboardLayout title="Departments">
      <div className="flex items-center justify-between mb-6 flex-wrap gap-3">
        <h2 className="text-xl font-semibold text-gray-800">Departments ({departments.length})</h2>
        <div className="flex gap-2">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
            <input placeholder="Search..." className="input pl-9 w-52" value={search} onChange={e => setSearch(e.target.value)} />
          </div>
          <button onClick={openCreate} className="btn-primary flex items-center gap-2">
            <Plus className="w-4 h-4" /> Add Department
          </button>
        </div>
      </div>

      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {loading ? (
          <div className="col-span-3 flex justify-center py-12">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600" />
          </div>
        ) : filtered.length === 0 ? (
          <p className="col-span-3 text-center text-gray-400 py-10">No departments found</p>
        ) : filtered.map(d => (
          <div key={d.id} className="card hover:shadow-md transition-shadow border-t-4 border-primary-400">
            <div className="flex items-start justify-between">
              <div>
                <span className="badge bg-primary-100 text-primary-700 mb-2">{d.code}</span>
                <h3 className="font-semibold text-gray-800">{d.name}</h3>
                <p className="text-xs text-gray-500 mt-1 line-clamp-2">{d.description}</p>
              </div>
              <div className="flex gap-1 ml-2">
                <button onClick={() => openEdit(d)} className="p-1.5 hover:bg-gray-100 rounded text-gray-500"><Pencil className="w-3.5 h-3.5" /></button>
                <button onClick={() => deleteDept(d.id)} className="p-1.5 hover:bg-red-50 rounded text-red-400"><Trash2 className="w-3.5 h-3.5" /></button>
              </div>
            </div>
            {d.headFacultyId && (
              <p className="text-xs text-gray-400 mt-3 pt-3 border-t border-gray-100">
                Head: <span className="text-gray-600 font-medium">{d.headFacultyId}</span>
              </p>
            )}
          </div>
        ))}
      </div>

      {showModal && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl shadow-xl w-full max-w-md">
            <div className="flex items-center justify-between p-5 border-b">
              <h3 className="font-semibold text-gray-800">{editing ? "Edit Department" : "Add Department"}</h3>
              <button onClick={() => setShowModal(false)}><X className="w-5 h-5 text-gray-400" /></button>
            </div>
            <form onSubmit={handleSubmit(onSubmit)} className="p-5 space-y-3">
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Name *</label>
                  <input {...register("name", { required: true })} className="input" />
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Code *</label>
                  <input {...register("code", { required: true })} className="input" placeholder="CS" />
                </div>
              </div>
              <div>
                <label className="block text-xs font-medium text-gray-700 mb-1">Head Faculty ID</label>
                <input {...register("headFacultyId")} className="input" placeholder="FAC001" />
              </div>
              <div>
                <label className="block text-xs font-medium text-gray-700 mb-1">Description</label>
                <textarea {...register("description")} className="input resize-none" rows={3} />
              </div>
              <div className="flex justify-end gap-2 pt-2">
                <button type="button" onClick={() => setShowModal(false)} className="btn-secondary text-sm">Cancel</button>
                <button type="submit" disabled={isSubmitting} className="btn-primary text-sm">
                  {isSubmitting ? "Saving..." : editing ? "Update" : "Create"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </DashboardLayout>
  );
}

