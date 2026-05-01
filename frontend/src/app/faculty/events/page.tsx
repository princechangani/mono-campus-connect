"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import api from "@/lib/api";
import toast from "react-hot-toast";
import { Calendar, Search } from "lucide-react";
import { formatDate } from "@/lib/utils";

export default function FacultyEventsPage() {
  const [events,  setEvents]  = useState<any[]>([]);
  const [search,  setSearch]  = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get("/events").then(r => setEvents(r.data)).catch(() => {}).finally(() => setLoading(false));
  }, []);

  const filtered = events.filter(e =>
    `${e.title} ${e.description ?? ""} ${e.postedBy ?? ""}`.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <DashboardLayout title="Events">
      <div className="flex items-center justify-between mb-6 flex-wrap gap-3">
        <div>
          <h2 className="text-xl font-semibold text-gray-800">College Events</h2>
          <p className="text-sm text-gray-400 mt-0.5">{events.length} events posted</p>
        </div>
        <div className="relative">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
          <input className="input pl-9 w-52" placeholder="Search events…"
            value={search} onChange={e => setSearch(e.target.value)} />
        </div>
      </div>

      {loading ? (
        <div className="flex justify-center py-16">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600" />
        </div>
      ) : filtered.length === 0 ? (
        <div className="card text-center py-14">
          <Calendar className="w-10 h-10 text-gray-300 mx-auto mb-3" />
          <p className="text-gray-400">No events found</p>
        </div>
      ) : (
        <div className="grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
          {filtered.map(e => (
            <div key={e.id} className="card hover:shadow-md transition-shadow !p-0 overflow-hidden">
              <div className="h-2 bg-gradient-to-r from-primary-500 to-primary-700" />
              <div className="p-4">
                <h3 className="font-semibold text-gray-800 leading-snug">{e.title}</h3>
                <p className="text-sm text-gray-500 mt-2 line-clamp-4">{e.description}</p>
                <div className="flex items-center justify-between mt-4 pt-3 border-t border-gray-100 text-xs text-gray-400">
                  <span>By <span className="text-gray-600 font-medium">{e.postedBy ?? "Admin"}</span></span>
                  <span>{formatDate(e.createdAt)}</span>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </DashboardLayout>
  );
}

