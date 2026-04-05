"use client";
import React, { useState, useEffect } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import { getUser } from "@/lib/auth";
import { facultyService } from "@/lib/services/facultyService";
import { Users, Search, Filter, Eye, Mail, Phone } from "lucide-react";

interface Faculty {
  id?: number;
  facultyPublicId?: string;
  userId: number;
  departmentId: number;
  employeeId: string;
  designation?: string;
  specialization?: string;
  joiningDate?: string;
  status?: string;
  tenantId?: string;
}

export default function HodFaculty() {
  const user = getUser();
  const [faculty, setFaculty] = useState<Faculty[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState("");
  const [statusFilter, setStatusFilter] = useState("all");

  useEffect(() => {
    fetchFaculty();
  }, []);

  const fetchFaculty = async () => {
    try {
      setLoading(true);
      const response = await facultyService.getAll();
      setFaculty(response.data);
    } catch (error) {
      console.error("Failed to fetch faculty:", error);
    } finally {
      setLoading(false);
    }
  };

  const filteredFaculty = faculty.filter(member => {
    const matchesSearch = member.employeeId.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         member.designation?.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesStatus = statusFilter === "all" || member.status === statusFilter;
    return matchesSearch && matchesStatus;
  });

  return (
    <DashboardLayout title="Faculty Management">
      <div className="mb-6">
        <h2 className="text-xl font-semibold text-gray-800">
          Department Faculty - {user?.firstName ?? "HOD"}
        </h2>
        <p className="text-gray-500 text-sm mt-1">View and manage all faculty members in your department.</p>
      </div>

      {/* Filters */}
      <div className="card p-4 mb-6">
        <div className="flex flex-col sm:flex-row gap-4">
          <div className="flex-1">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
              <input
                type="text"
                placeholder="Search by employee ID or designation..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
              />
            </div>
          </div>
          <div className="flex items-center gap-2">
            <Filter className="w-4 h-4 text-gray-500" />
            <select
              value={statusFilter}
              onChange={(e) => setStatusFilter(e.target.value)}
              className="px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
            >
              <option value="all">All Status</option>
              <option value="ACTIVE">Active</option>
              <option value="INACTIVE">Inactive</option>
              <option value="ON_LEAVE">On Leave</option>
            </select>
          </div>
        </div>
      </div>

      {/* Faculty Table */}
      <div className="card">
        <div className="overflow-x-auto">
          <table className="w-full">
            <thead>
              <tr className="border-b border-gray-200">
                <th className="text-left py-3 px-4 font-medium text-gray-700">Employee ID</th>
                <th className="text-left py-3 px-4 font-medium text-gray-700">Name</th>
                <th className="text-left py-3 px-4 font-medium text-gray-700">Designation</th>
                <th className="text-left py-3 px-4 font-medium text-gray-700">Specialization</th>
                <th className="text-left py-3 px-4 font-medium text-gray-700">Status</th>
                <th className="text-left py-3 px-4 font-medium text-gray-700">Actions</th>
              </tr>
            </thead>
            <tbody>
              {loading ? (
                <tr>
                  <td colSpan={6} className="text-center py-8 text-gray-500">
                    Loading faculty...
                  </td>
                </tr>
              ) : filteredFaculty.length === 0 ? (
                <tr>
                  <td colSpan={6} className="text-center py-8 text-gray-500">
                    No faculty members found
                  </td>
                </tr>
              ) : (
                filteredFaculty.map((member) => (
                  <tr key={member.id} className="border-b border-gray-100 hover:bg-gray-50">
                    <td className="py-3 px-4">
                      <span className="font-medium text-gray-900">{member.employeeId}</span>
                    </td>
                    <td className="py-3 px-4 text-gray-700">
                      {/* Will be populated when user data is joined */}
                      Faculty Name
                    </td>
                    <td className="py-3 px-4 text-gray-700">{member.designation || "N/A"}</td>
                    <td className="py-3 px-4 text-gray-700">{member.specialization || "N/A"}</td>
                    <td className="py-3 px-4">
                      <span className={`inline-flex px-2 py-1 text-xs font-medium rounded-full ${
                        member.status === "ACTIVE" 
                          ? "bg-green-100 text-green-800"
                          : member.status === "INACTIVE"
                          ? "bg-red-100 text-red-800"
                          : member.status === "ON_LEAVE"
                          ? "bg-yellow-100 text-yellow-800"
                          : "bg-gray-100 text-gray-800"
                      }`}>
                        {member.status || "UNKNOWN"}
                      </span>
                    </td>
                    <td className="py-3 px-4">
                      <div className="flex gap-2">
                        <button className="text-primary-600 hover:text-primary-700 p-1">
                          <Eye className="w-4 h-4" />
                        </button>
                        <button className="text-gray-600 hover:text-gray-700 p-1">
                          <Mail className="w-4 h-4" />
                        </button>
                        <button className="text-gray-600 hover:text-gray-700 p-1">
                          <Phone className="w-4 h-4" />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </DashboardLayout>
  );
}
