"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import api from "@/lib/api";
import toast from "react-hot-toast";
import { Bell, CheckCheck, Trash2 } from "lucide-react";
import { formatDateTime } from "@/lib/utils";

const typeColors: Record<string, string> = {
  EXAM:       "bg-yellow-100 text-yellow-700",
  RESULT:     "bg-green-100 text-green-700",
  ATTENDANCE: "bg-blue-100 text-blue-700",
  EVENT:      "bg-purple-100 text-purple-700",
  GENERAL:    "bg-gray-100 text-gray-700",
};

export default function NotificationsPage() {
  const [notifs, setNotifs]   = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  const load = () => {
    api.get("/notifications").then(r => setNotifs(r.data)).catch(() => {}).finally(() => setLoading(false));
  };

  useEffect(() => { load(); }, []);

  const markRead = (id: number) => {
    api.put(`/notifications/${id}/read`).then(() =>
      setNotifs(prev => prev.map(n => n.id === id ? { ...n, read: true } : n))
    ).catch(() => {});
  };

  const markAllRead = () => {
    api.put("/notifications/read-all").then(() => {
      setNotifs(prev => prev.map(n => ({ ...n, read: true })));
      toast.success("All marked as read");
    }).catch(() => {});
  };

  const deleteNotif = (id: number) => {
    api.delete(`/notifications/${id}`).then(() => {
      setNotifs(prev => prev.filter(n => n.id !== id));
      toast.success("Notification deleted");
    }).catch(() => {});
  };

  const unreadCount = notifs.filter(n => !n.read).length;

  return (
    <DashboardLayout title="Notifications">
      <div className="max-w-2xl mx-auto">
        <div className="flex items-center justify-between mb-6">
          <div>
            <h2 className="text-xl font-semibold text-gray-800">Notifications</h2>
            {unreadCount > 0 && (
              <p className="text-sm text-gray-500 mt-0.5">{unreadCount} unread</p>
            )}
          </div>
          {unreadCount > 0 && (
            <button onClick={markAllRead} className="btn-secondary flex items-center gap-2 text-sm">
              <CheckCheck className="w-4 h-4" /> Mark all read
            </button>
          )}
        </div>

        {loading ? (
          <div className="flex justify-center py-12">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600" />
          </div>
        ) : notifs.length === 0 ? (
          <div className="card text-center py-16">
            <Bell className="w-10 h-10 text-gray-300 mx-auto mb-3" />
            <p className="text-gray-500">All caught up! No notifications.</p>
          </div>
        ) : (
          <div className="space-y-2">
            {notifs.map((n) => (
              <div
                key={n.id}
                className={`card !p-4 flex items-start gap-3 transition-all ${!n.read ? "border-l-4 border-primary-400 bg-blue-50/40" : ""}`}
              >
                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2 mb-1">
                    <span className={`badge ${typeColors[n.type] ?? typeColors.GENERAL}`}>{n.type}</span>
                    {!n.read && <span className="w-2 h-2 bg-primary-500 rounded-full" />}
                  </div>
                  <p className="text-sm font-medium text-gray-800">{n.title}</p>
                  <p className="text-sm text-gray-500 mt-0.5">{n.message}</p>
                  <p className="text-xs text-gray-400 mt-1">{formatDateTime(n.createdAt)}</p>
                </div>
                <div className="flex items-center gap-1 flex-shrink-0">
                  {!n.read && (
                    <button onClick={() => markRead(n.id)}
                      className="p-1.5 text-gray-400 hover:text-primary-600 hover:bg-primary-50 rounded-lg" title="Mark as read">
                      <CheckCheck className="w-4 h-4" />
                    </button>
                  )}
                  <button onClick={() => deleteNotif(n.id)}
                    className="p-1.5 text-gray-400 hover:text-red-500 hover:bg-red-50 rounded-lg" title="Delete">
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </DashboardLayout>
  );
}

