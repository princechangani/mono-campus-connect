"use client";
import React from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import { getUser } from "@/lib/auth";

export default function HodDashboard() {
  const user = getUser();

  return (
    <DashboardLayout title="HOD Dashboard">
      <div className="mb-6">
        <h2 className="text-xl font-semibold text-gray-800">
          Welcome, {user?.firstName ?? "HOD"} 👋
        </h2>
        <p className="text-gray-500 text-sm mt-1">Manage your department's faculty, students, and courses.</p>
      </div>
      
      <div className="card p-6 flex flex-col items-center justify-center text-center">
        <h3 className="text-lg font-medium text-gray-700">Dashboard Insights Coming Soon</h3>
        <p className="text-gray-500 mt-2">
          Your department stats and data will be displayed here soon.
        </p>
      </div>
    </DashboardLayout>
  );
}
