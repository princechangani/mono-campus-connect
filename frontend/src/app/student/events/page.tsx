"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import api from "@/lib/api";
import { formatDate } from "@/lib/utils";
import { Calendar } from "lucide-react";

export default function EventsPage() {
  const [events,  setEvents]  = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get("/events").then(r => setEvents(r.data)).catch(() => {}).finally(() => setLoading(false));
  }, []);

  return (
    <DashboardLayout title="Events">
      <div className="mb-6">
        <h2 className="text-xl font-semibold text-gray-800">Campus Events</h2>
        <p className="text-gray-500 text-sm mt-1">Stay up-to-date with what's happening on campus.</p>
      </div>

      {loading ? (
        <div className="flex justify-center py-12"><div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600" /></div>
      ) : events.length === 0 ? (
        <div className="card text-center py-12">
          <Calendar className="w-10 h-10 text-gray-300 mx-auto mb-3" />
          <p className="text-gray-400">No events posted yet</p>
        </div>
      ) : (
        <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
          {events.map(e => (
            <div key={e.id} className="card hover:shadow-md transition-shadow overflow-hidden !p-0">
              {e.imageContent && (
                <img
                  src={`data:image/jpeg;base64,${e.imageContent}`}
                  alt={e.title}
                  className="w-full h-40 object-cover"
                />
              )}
              {!e.imageContent && (
                <div className="w-full h-40 bg-gradient-to-br from-primary-100 to-primary-200 flex items-center justify-center">
                  <Calendar className="w-12 h-12 text-primary-400" />
                </div>
              )}
              <div className="p-4">
                <h3 className="font-semibold text-gray-800 mb-1">{e.title}</h3>
                <p className="text-sm text-gray-500 line-clamp-3">{e.description}</p>
                <div className="flex items-center justify-between mt-3 text-xs text-gray-400">
                  <span>By {e.postedBy}</span>
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

