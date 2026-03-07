"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import api from "@/lib/api";
import toast from "react-hot-toast";
import { Plus, Pencil, Trash2, X, Calendar, Search } from "lucide-react";
import { useForm } from "react-hook-form";
import { formatDate } from "@/lib/utils";

export default function AdminEventsPage() {
  const [events,    setEvents]    = useState<any[]>([]);
  const [search,    setSearch]    = useState("");
  const [loading,   setLoading]   = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editing,   setEditing]   = useState<any>(null);

  const { register, handleSubmit, reset, formState: { isSubmitting } } = useForm<any>();

  const load = () => {
    api.get("/events").then(r => setEvents(r.data)).catch(() => {}).finally(() => setLoading(false));
  };
  useEffect(() => { load(); }, []);

  const openCreate = () => { setEditing(null); reset({}); setShowModal(true); };
  const openEdit   = (e: any) => { setEditing(e); reset(e); setShowModal(true); };

  const onSubmit = async (data: any) => {
    try {
      if (editing) {
        await api.put(`/events/${editing.id}`, data);
        toast.success("Event updated");
      } else {
        await api.post("/events", data);
        toast.success("Event created");
      }
      setShowModal(false); load();
    } catch { toast.error("Operation failed"); }
  };

  const deleteEvent = (id: number) => {
    if (!confirm("Delete this event?")) return;
    api.delete(`/events/${id}`).then(() => { toast.success("Deleted"); load(); }).catch(() => toast.error("Delete failed"));
  };

  const filtered = events.filter(e =>
    `${e.title} ${e.description} ${e.postedBy}`.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <DashboardLayout title="Events">
      <div className="flex items-center justify-between mb-6 flex-wrap gap-3">
        <h2 className="text-xl font-semibold text-gray-800">Events ({events.length})</h2>
        <div className="flex gap-2">
          <div className="relative">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
            <input placeholder="Search..." className="input pl-9 w-52" value={search} onChange={e => setSearch(e.target.value)} />
          </div>
          <button onClick={openCreate} className="btn-primary flex items-center gap-2">
            <Plus className="w-4 h-4" /> New Event
          </button>
        </div>
      </div>

      {loading ? (
        <div className="flex justify-center py-12"><div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600" /></div>
      ) : filtered.length === 0 ? (
        <div className="card text-center py-12">
          <Calendar className="w-10 h-10 text-gray-300 mx-auto mb-3" />
          <p className="text-gray-400">No events posted yet</p>
        </div>
      ) : (
        <div className="grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
          {filtered.map(e => (
            <div key={e.id} className="card hover:shadow-md transition-shadow overflow-hidden !p-0">
              {/* Colour header */}
              <div className="h-2 bg-gradient-to-r from-primary-500 to-primary-700" />
              <div className="p-4">
                <div className="flex items-start justify-between gap-2">
                  <h3 className="font-semibold text-gray-800 leading-snug flex-1">{e.title}</h3>
                  <div className="flex gap-1 flex-shrink-0">
                    <button onClick={() => openEdit(e)} className="p-1.5 hover:bg-gray-100 rounded text-gray-400"><Pencil className="w-3.5 h-3.5" /></button>
                    <button onClick={() => deleteEvent(e.id)} className="p-1.5 hover:bg-red-50 rounded text-red-400"><Trash2 className="w-3.5 h-3.5" /></button>
                  </div>
                </div>
                <p className="text-sm text-gray-500 mt-2 line-clamp-3">{e.description}</p>
                <div className="flex items-center justify-between mt-3 pt-3 border-t border-gray-100 text-xs text-gray-400">
                  <span>By <span className="text-gray-600 font-medium">{e.postedBy}</span></span>
                  <span>{formatDate(e.createdAt)}</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {showModal && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl shadow-xl w-full max-w-lg">
            <div className="flex items-center justify-between p-5 border-b">
              <h3 className="font-semibold text-gray-800">{editing ? "Edit Event" : "New Event"}</h3>
              <button onClick={() => setShowModal(false)}><X className="w-5 h-5 text-gray-400" /></button>
            </div>
            <form onSubmit={handleSubmit(onSubmit)} className="p-5 space-y-3">
              <div>
                <label className="block text-xs font-medium text-gray-700 mb-1">Title *</label>
                <input {...register("title", { required: true })} className="input" placeholder="Annual Tech Fest 2026" />
              </div>
              <div>
                <label className="block text-xs font-medium text-gray-700 mb-1">Description *</label>
                <textarea {...register("description", { required: true })} className="input resize-none" rows={4}
                  placeholder="Describe the event..." />
              </div>
              <div>
                <label className="block text-xs font-medium text-gray-700 mb-1">Posted By</label>
                <input {...register("postedBy")} className="input" placeholder="admin@sunrise.edu" />
              </div>
              <div className="flex justify-end gap-2 pt-2">
                <button type="button" onClick={() => setShowModal(false)} className="btn-secondary text-sm">Cancel</button>
                <button type="submit" disabled={isSubmitting} className="btn-primary text-sm">
                  {isSubmitting ? "Saving..." : editing ? "Update" : "Post Event"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </DashboardLayout>
  );
}

