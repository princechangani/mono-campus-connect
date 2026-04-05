"use client";
import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { getToken, getRole } from "@/lib/auth";
import Sidebar from "./Sidebar";
import { Bell, Search } from "lucide-react";
import api from "@/lib/api";
import { useQuery } from "react-query";

interface Props {
  children: React.ReactNode;
  title?: string;
}

export default function DashboardLayout({ children, title }: Props) {
  const router   = useRouter();
  const [role, setRole]         = useState<string | null>(null);
  const [checked, setChecked]   = useState(false);
  const unreadCount = 0;

  useEffect(() => {
    const token = getToken();
    const r     = getRole();
    if (!token || !r) {
      router.replace("/login");
      return;
    }
    setRole(r);
    setChecked(true);
  }, [router]);

  if (!checked || !role) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="animate-spin rounded-full h-10 w-10 border-b-2 border-primary-600" />
      </div>
    );
  }

  return (
    <div className="flex min-h-screen bg-gray-50">
      <Sidebar role={role} />

      <div className="flex-1 flex flex-col min-w-0">
        {/* Topbar */}
        <header className="h-16 bg-white border-b border-gray-200 flex items-center justify-between px-6 sticky top-0 z-10">
          <h1 className="text-lg font-semibold text-gray-800">{title ?? "Dashboard"}</h1>
          <div className="flex items-center gap-3">
            <div className="relative hidden sm:block">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
              <input placeholder="Search..." className="input pl-9 w-56 h-9 text-sm" />
            </div>
            <button
              onClick={() => router.push("/notifications")}
              className="relative p-2 text-gray-500 hover:bg-gray-100 rounded-lg"
            >
              <Bell className="w-5 h-5" />
              {unreadCount > 0 && (
                <span className="absolute top-1.5 right-1.5 w-2 h-2 bg-red-500 rounded-full" />
              )}
            </button>
          </div>
        </header>

        {/* Page content */}
        <main className="flex-1 p-6 overflow-y-auto">
          {children}
        </main>
      </div>
    </div>
  );
}
