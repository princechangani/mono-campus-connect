"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import StatCard from "@/components/ui/StatCard";
import api from "@/lib/api";
import { Building2, Users, GraduationCap, Globe } from "lucide-react";
import { formatDate } from "@/lib/utils";

export default function SuperAdminDashboard() {
  const [tenants, setTenants] = useState<any[]>([]);

  useEffect(() => {
    api.get("/tenants").then(r => setTenants(r.data)).catch(() => {});
  }, []);

  const active   = tenants.filter(t => t.active).length;
  const inactive = tenants.filter(t => !t.active).length;

  return (
    <DashboardLayout title="Super Admin Dashboard">
      <div className="mb-6">
        <h2 className="text-xl font-semibold text-gray-800">Platform Overview</h2>
        <p className="text-gray-500 text-sm mt-1">Manage all colleges on the CampusConnect platform.</p>
      </div>

      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
        <StatCard title="Total Colleges"  value={tenants.length} icon={<Building2 className="w-5 h-5" />} color="blue" />
        <StatCard title="Active"          value={active}         icon={<Globe className="w-5 h-5" />}    color="green" />
        <StatCard title="Inactive"        value={inactive}       icon={<Building2 className="w-5 h-5" />} color="red" />
        <StatCard title="Total Users"     value="—"              icon={<Users className="w-5 h-5" />}    color="purple" />
      </div>

      <div className="card">
        <div className="flex items-center justify-between mb-4">
          <h3 className="font-semibold text-gray-800">All Colleges</h3>
          <a href="/super-admin/tenants" className="text-primary-600 text-sm hover:underline">Manage</a>
        </div>
        {tenants.length === 0 ? (
          <p className="text-gray-400 text-sm text-center py-8">No colleges onboarded yet</p>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-gray-100">
                  <th className="text-left py-2 px-3 text-gray-500 font-medium">College</th>
                  <th className="text-left py-2 px-3 text-gray-500 font-medium">Domain</th>
                  <th className="text-left py-2 px-3 text-gray-500 font-medium">Created</th>
                  <th className="text-left py-2 px-3 text-gray-500 font-medium">Status</th>
                  <th className="text-left py-2 px-3 text-gray-500 font-medium">Actions</th>
                </tr>
              </thead>
              <tbody>
                {tenants.map((t) => (
                  <tr key={t.id} className="border-b border-gray-50 hover:bg-gray-50">
                    <td className="py-2.5 px-3 font-medium text-gray-800">{t.name}</td>
                    <td className="py-2.5 px-3 text-gray-500">{t.domain}</td>
                    <td className="py-2.5 px-3 text-gray-500">{formatDate(t.createdAt)}</td>
                    <td className="py-2.5 px-3">
                      <span className={`badge ${t.active ? "bg-green-100 text-green-700" : "bg-red-100 text-red-700"}`}>
                        {t.active ? "Active" : "Inactive"}
                      </span>
                    </td>
                    <td className="py-2.5 px-3">
                      <button
                        onClick={() => {
                          const endpoint = t.active ? `/tenants/${t.id}/disable` : `/tenants/${t.id}/enable`;
                          api.put(endpoint).then(() => setTenants(prev =>
                            prev.map(x => x.id === t.id ? { ...x, active: !x.active } : x)
                          )).catch(() => {});
                        }}
                        className={`text-xs px-2 py-1 rounded font-medium ${t.active ? "text-red-600 hover:bg-red-50" : "text-green-600 hover:bg-green-50"}`}
                      >
                        {t.active ? "Disable" : "Enable"}
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </DashboardLayout>
  );
}

