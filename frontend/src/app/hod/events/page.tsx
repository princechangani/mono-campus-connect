"use client";
import React, { useState, useEffect } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import { getUser } from "@/lib/auth";
import { announcementService } from "@/lib/services/announcementService";
import { Calendar, Search, Filter, Eye, Plus, Edit, Trash2 } from "lucide-react";

interface EventItem {
  id?: number;
  title?: string;
  content?: string;
  type?: string;
  priority?: string;
  startDate?: string;
  endDate?: string;
  status?: string;
  createdBy?: number;
  tenantId?: string;
}

export default function HodEvents() {
  const user = getUser();
  const [events, setEvents] = useState<EventItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [typeFilter, setTypeFilter] = useState("all");
  const [priorityFilter, setPriorityFilter] = useState("all");

  useEffect(() => {
    fetchEvents();
  }, []);

  const fetchEvents = async () => {
    try {
      setLoading(true);
      const response = await announcementService.getAll();
      setEvents(response.data);
    } catch (error) {
      console.error("Failed to fetch events:", error);
    } finally {
      setLoading(false);
    }
  };

  const filteredEvents = events.filter(event => {
    const title = event.title ?? "";
    const content = event.content ?? "";
    const matchesSearch = title.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         content.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesType = typeFilter === "all" || event.type === typeFilter;
    const matchesPriority = priorityFilter === "all" || event.priority === priorityFilter;
    return matchesSearch && matchesType && matchesPriority;
  });

  const getPriorityColor = (priority: string) => {
    switch (priority) {
      case "HIGH": return "bg-red-100 text-red-800";
      case "MEDIUM": return "bg-yellow-100 text-yellow-800";
      case "LOW": return "bg-green-100 text-green-800";
      default: return "bg-gray-100 text-gray-800";
    }
  };

  const getTypeColor = (type: string) => {
    switch (type) {
      case "EVENT": return "bg-blue-100 text-blue-800";
      case "HOLIDAY": return "bg-purple-100 text-purple-800";
      case "EXAM": return "bg-orange-100 text-orange-800";
      case "GENERAL": return "bg-gray-100 text-gray-800";
      default: return "bg-gray-100 text-gray-800";
    }
  };

  return (
    <DashboardLayout title="Events Management">
      <div className="mb-6">
        <h2 className="text-xl font-semibold text-gray-800">
          Department Events - {user?.firstName ?? "HOD"}
        </h2>
        <p className="text-gray-500 text-sm mt-1">Create and manage events and announcements for your department.</p>
      </div>

      {/* Action Bar */}
      <div className="flex justify-between items-center mb-6">
        <div className="flex flex-col sm:flex-row gap-4 flex-1">
          <div className="flex-1 max-w-md">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
              <input
                type="text"
                placeholder="Search events..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
              />
            </div>
          </div>
          <div className="flex items-center gap-2">
            <Filter className="w-4 h-4 text-gray-500" />
            <select
              value={typeFilter}
              onChange={(e) => setTypeFilter(e.target.value)}
              className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
            >
              <option value="all">All Types</option>
              <option value="EVENT">Events</option>
              <option value="HOLIDAY">Holidays</option>
              <option value="EXAM">Exams</option>
              <option value="GENERAL">General</option>
            </select>
          </div>
          <div className="flex items-center gap-2">
            <select
              value={priorityFilter}
              onChange={(e) => setPriorityFilter(e.target.value)}
              className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
            >
              <option value="all">All Priorities</option>
              <option value="HIGH">High</option>
              <option value="MEDIUM">Medium</option>
              <option value="LOW">Low</option>
            </select>
          </div>
        </div>
        <button className="bg-primary-600 text-white px-4 py-2 rounded-lg hover:bg-primary-700 flex items-center gap-2">
          <Plus className="w-4 h-4" />
          Create Event
        </button>
      </div>

      {/* Events Grid */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
        {loading ? (
          <div className="col-span-full text-center py-12 text-gray-500">
            Loading events...
          </div>
        ) : filteredEvents.length === 0 ? (
          <div className="col-span-full text-center py-12 text-gray-500">
            No events found
          </div>
        ) : (
          filteredEvents.map((event) => (
            <div key={event.id} className="card p-5 hover:shadow-lg transition-shadow">
              <div className="flex justify-between items-start mb-3">
                <h3 className="font-semibold text-gray-900 line-clamp-2">{event.title ?? "Untitled"}</h3>
                <div className="flex gap-1 ml-2">
                  <button className="text-gray-500 hover:text-gray-700 p-1">
                    <Edit className="w-4 h-4" />
                  </button>
                  <button className="text-red-500 hover:text-red-700 p-1">
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>
              </div>
              
              <p className="text-gray-600 text-sm mb-4 line-clamp-3">{event.content ?? "—"}</p>
              
              <div className="flex flex-wrap gap-2 mb-3">
                <span className={`inline-flex px-2 py-1 text-xs font-medium rounded-full ${getTypeColor(event.type ?? "GENERAL")}`}>
                  {event.type ?? "GENERAL"}
                </span>
                <span className={`inline-flex px-2 py-1 text-xs font-medium rounded-full ${getPriorityColor(event.priority ?? "LOW")}`}>
                  {event.priority ?? "LOW"}
                </span>
              </div>
              
              {event.startDate && (
                <div className="flex items-center gap-2 text-sm text-gray-500">
                  <Calendar className="w-4 h-4" />
                  <span>
                    {new Date(event.startDate).toLocaleDateString()}
                    {event.endDate && ` - ${new Date(event.endDate).toLocaleDateString()}`}
                  </span>
                </div>
              )}
              
              <div className="mt-4 pt-3 border-t border-gray-100">
                <button className="text-primary-600 hover:text-primary-700 text-sm font-medium flex items-center gap-1">
                  <Eye className="w-4 h-4" />
                  View Details
                </button>
              </div>
            </div>
          ))
        )}
      </div>
    </DashboardLayout>
  );
}
