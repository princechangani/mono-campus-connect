"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import api from "@/lib/api";
import toast from "react-hot-toast";
import { Plus, Search, Pencil, Trash2, ToggleLeft, ToggleRight, X } from "lucide-react";
import { useForm } from "react-hook-form";
import { getInitials } from "@/lib/utils";

export default function AdminStudentsPage() {
  const [students, setStudents] = useState<any[]>([]);
  const [search,   setSearch]   = useState("");
  const [loading,  setLoading]  = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editing,   setEditing]   = useState<any>(null);

  const { register, handleSubmit, reset, formState: { isSubmitting } } = useForm<any>();

  const load = () => {
    api.get("/admin/users/role/STUDENT").then(r => setStudents(r.data)).catch(() => {}).finally(() => setLoading(false));
  };
  useEffect(() => { load(); }, []);

  const openCreate = () => { setEditing(null); reset({}); setShowModal(true); };
  const openEdit   = (u: any) => { setEditing(u); reset(u); setShowModal(true); };

  const onSubmit = async (data: any) => {
    try {
      if (editing) {
        await api.put(`/admin/users/${editing.id}`, { ...data, role: "STUDENT" });
        toast.success("Student updated");
      } else {
        await api.post("/admin/users", { ...data, role: "STUDENT" });
        toast.success("Student created");
      }
      setShowModal(false);
      load();
    } catch { toast.error("Operation failed"); }
  };

  const toggleEnabled = (u: any) => {
    const ep = u.enabled ? `/admin/users/${u.id}/disable` : `/admin/users/${u.id}/enable`;
    api.put(ep).then(() => { toast.success(`User ${u.enabled ? "disabled" : "enabled"}`); load(); }).catch(() => {});
  };

  const deleteUser = (id: number) => {
    if (!confirm("Delete this student?")) return;
    api.delete(`/admin/users/${id}`).then(() => { toast.success("Deleted"); load(); }).catch(() => {});
  };

  const filtered = students.filter(s =>
    `${s.firstName} ${s.lastName} ${s.email} ${s.enrollmentNumber ?? ""}`.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <DashboardLayout title="Students">
      <div className="flex items-center justify-between mb-6 flex-wrap gap-3">
        <h2 className="text-xl font-semibold text-gray-800">Students ({students.length})</h2>
        <div className="flex gap-2">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
            <input placeholder="Search..." className="input pl-9 w-52" value={search} onChange={e => setSearch(e.target.value)} />
          </div>
          <button onClick={openCreate} className="btn-primary flex items-center gap-2">
            <Plus className="w-4 h-4" /> Add Student
          </button>
        </div>
      </div>

      <div className="card overflow-x-auto">
        {loading ? (
          <div className="flex justify-center py-12"><div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600" /></div>
        ) : filtered.length === 0 ? (
          <p className="text-gray-400 text-sm text-center py-10">No students found</p>
        ) : (
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b border-gray-100">
                <th className="text-left py-2 px-3 text-gray-500 font-medium">Name</th>
                <th className="text-left py-2 px-3 text-gray-500 font-medium">Email</th>
                <th className="text-left py-2 px-3 text-gray-500 font-medium">Enrollment</th>
                <th className="text-left py-2 px-3 text-gray-500 font-medium">Department</th>
                <th className="text-left py-2 px-3 text-gray-500 font-medium">Semester</th>
                <th className="text-left py-2 px-3 text-gray-500 font-medium">Status</th>
                <th className="text-left py-2 px-3 text-gray-500 font-medium">Actions</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map(u => (
                <tr key={u.id} className="border-b border-gray-50 hover:bg-gray-50">
                  <td className="py-2.5 px-3">
                    <div className="flex items-center gap-2">
                      <div className="w-7 h-7 rounded-full bg-primary-100 flex items-center justify-center text-primary-700 text-xs font-bold">
                        {getInitials(`${u.firstName} ${u.lastName}`)}
                      </div>
                      <span className="font-medium text-gray-800">{u.firstName} {u.lastName}</span>
                    </div>
                  </td>
                  <td className="py-2.5 px-3 text-gray-500">{u.email}</td>
                  <td className="py-2.5 px-3 text-gray-500">{u.enrollmentNumber ?? "—"}</td>
                  <td className="py-2.5 px-3 text-gray-500">{u.department ?? "—"}</td>
                  <td className="py-2.5 px-3 text-gray-500">{u.semester ?? "—"}</td>
                  <td className="py-2.5 px-3">
                    <span className={`badge ${u.enabled ? "bg-green-100 text-green-700" : "bg-red-100 text-red-700"}`}>
                      {u.enabled ? "Active" : "Disabled"}
                    </span>
                  </td>
                  <td className="py-2.5 px-3">
                    <div className="flex items-center gap-1">
                      <button onClick={() => openEdit(u)} className="p-1.5 hover:bg-gray-100 rounded text-gray-500"><Pencil className="w-3.5 h-3.5" /></button>
                      <button onClick={() => toggleEnabled(u)} className="p-1.5 hover:bg-gray-100 rounded text-gray-500">
                        {u.enabled ? <ToggleRight className="w-3.5 h-3.5 text-green-500" /> : <ToggleLeft className="w-3.5 h-3.5 text-gray-400" />}
                      </button>
                      <button onClick={() => deleteUser(u.id)} className="p-1.5 hover:bg-red-50 rounded text-red-400"><Trash2 className="w-3.5 h-3.5" /></button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {/* Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl shadow-xl w-full max-w-md">
            <div className="flex items-center justify-between p-5 border-b">
              <h3 className="font-semibold text-gray-800">{editing ? "Edit Student" : "Add Student"}</h3>
              <button onClick={() => setShowModal(false)} className="text-gray-400 hover:text-gray-600"><X className="w-5 h-5" /></button>
            </div>
            <form onSubmit={handleSubmit(onSubmit)} className="p-5 space-y-3">
              <div className="grid grid-cols-2 gap-3">
                <div><label className="block text-xs font-medium text-gray-700 mb-1">First Name</label><input {...register("firstName", { required: true })} className="input" /></div>
                <div><label className="block text-xs font-medium text-gray-700 mb-1">Last Name</label><input {...register("lastName", { required: true })} className="input" /></div>
              </div>
              <div><label className="block text-xs font-medium text-gray-700 mb-1">Email</label><input {...register("email", { required: true })} type="email" className="input" /></div>
              {!editing && <div><label className="block text-xs font-medium text-gray-700 mb-1">Password</label><input {...register("password", { required: !editing })} type="password" className="input" /></div>}
              <div className="grid grid-cols-2 gap-3">
                <div><label className="block text-xs font-medium text-gray-700 mb-1">Enrollment No.</label><input {...register("enrollmentNumber")} className="input" /></div>
                <div><label className="block text-xs font-medium text-gray-700 mb-1">Semester</label><input {...register("semester")} className="input" /></div>
              </div>
              <div><label className="block text-xs font-medium text-gray-700 mb-1">Department</label><input {...register("department")} className="input" /></div>
              <div className="flex justify-end gap-2 pt-2">
                <button type="button" onClick={() => setShowModal(false)} className="btn-secondary text-sm">Cancel</button>
                <button type="submit" disabled={isSubmitting} className="btn-primary text-sm">{isSubmitting ? "Saving..." : editing ? "Update" : "Create"}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </DashboardLayout>
  );
}

