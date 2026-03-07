"use client";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { cn } from "@/lib/utils";
import { logout, getUser } from "@/lib/auth";
import { getInitials } from "@/lib/utils";
import {
  LayoutDashboard, Users, BookOpen, ClipboardList,
  FileText, Calendar, Bell, Settings, LogOut,
  GraduationCap, BarChart2, Building2, Clock,
} from "lucide-react";

interface NavItem {
  label: string;
  href:  string;
  icon:  React.ReactNode;
}

const navByRole: Record<string, NavItem[]> = {
  STUDENT: [
    { label: "Dashboard",     href: "/student/dashboard",   icon: <LayoutDashboard className="w-4 h-4" /> },
    { label: "Subjects",      href: "/student/courses",     icon: <BookOpen className="w-4 h-4" /> },
    { label: "Attendance",    href: "/student/attendance",  icon: <ClipboardList className="w-4 h-4" /> },
    { label: "Exams",         href: "/student/exams",       icon: <FileText className="w-4 h-4" /> },
    { label: "Results",       href: "/student/results",     icon: <BarChart2 className="w-4 h-4" /> },
    { label: "Timetable",     href: "/student/timetable",   icon: <Clock className="w-4 h-4" /> },
    { label: "Materials",     href: "/student/materials",   icon: <BookOpen className="w-4 h-4" /> },
    { label: "Events",        href: "/student/events",      icon: <Calendar className="w-4 h-4" /> },
    { label: "Notifications", href: "/notifications",       icon: <Bell className="w-4 h-4" /> },
    { label: "Profile",       href: "/profile",             icon: <Settings className="w-4 h-4" /> },
  ],
  FACULTY: [
    { label: "Dashboard",     href: "/faculty/dashboard",   icon: <LayoutDashboard className="w-4 h-4" /> },
    { label: "My Subjects",   href: "/faculty/courses",     icon: <BookOpen className="w-4 h-4" /> },
    { label: "Attendance",    href: "/faculty/attendance",  icon: <ClipboardList className="w-4 h-4" /> },
    { label: "Exams",         href: "/faculty/exams",       icon: <FileText className="w-4 h-4" /> },
    { label: "Results",       href: "/faculty/results",     icon: <BarChart2 className="w-4 h-4" /> },
    { label: "Timetable",     href: "/faculty/timetable",   icon: <Clock className="w-4 h-4" /> },
    { label: "Materials",     href: "/faculty/materials",   icon: <BookOpen className="w-4 h-4" /> },
    { label: "Events",        href: "/faculty/events",      icon: <Calendar className="w-4 h-4" /> },
    { label: "Notifications", href: "/notifications",       icon: <Bell className="w-4 h-4" /> },
    { label: "Profile",       href: "/profile",             icon: <Settings className="w-4 h-4" /> },
  ],
  ADMIN: [
    { label: "Dashboard",     href: "/admin/dashboard",     icon: <LayoutDashboard className="w-4 h-4" /> },
    { label: "Students",      href: "/admin/students",      icon: <GraduationCap className="w-4 h-4" /> },
    { label: "Faculty",       href: "/admin/faculty",       icon: <Users className="w-4 h-4" /> },
    { label: "Departments",   href: "/admin/departments",   icon: <Building2 className="w-4 h-4" /> },
    { label: "Subjects",      href: "/admin/courses",       icon: <BookOpen className="w-4 h-4" /> },
    { label: "Timetable",     href: "/admin/timetable",     icon: <Clock className="w-4 h-4" /> },
    { label: "Events",        href: "/admin/events",        icon: <Calendar className="w-4 h-4" /> },
    { label: "Notifications", href: "/notifications",       icon: <Bell className="w-4 h-4" /> },
    { label: "Profile",       href: "/profile",             icon: <Settings className="w-4 h-4" /> },
  ],
  SUPER_ADMIN: [
    { label: "Dashboard",   href: "/super-admin/dashboard", icon: <LayoutDashboard className="w-4 h-4" /> },
    { label: "Colleges",    href: "/super-admin/tenants",   icon: <Building2 className="w-4 h-4" /> },
    { label: "Profile",     href: "/profile",               icon: <Settings className="w-4 h-4" /> },
  ],
};

interface SidebarProps {
  role: string;
}

export default function Sidebar({ role }: SidebarProps) {
  const pathname = usePathname();
  const user     = getUser();
  const navItems = navByRole[role] ?? navByRole["STUDENT"];

  return (
    <aside className="w-64 min-h-screen bg-sidebar flex flex-col">
      {/* Brand */}
      <div className="flex items-center gap-3 px-5 py-5 border-b border-slate-700">
        <div className="w-9 h-9 bg-primary-600 rounded-lg flex items-center justify-center">
          <GraduationCap className="w-5 h-5 text-white" />
        </div>
        <div>
          <p className="text-white font-semibold text-sm leading-none">CampusConnect</p>
          <p className="text-slate-400 text-xs mt-0.5 capitalize">{role.toLowerCase().replace("_", " ")}</p>
        </div>
      </div>

      {/* Nav */}
      <nav className="flex-1 px-3 py-4 space-y-0.5 overflow-y-auto">
        {navItems.map((item) => (
          <Link
            key={item.href}
            href={item.href}
            className={cn("sidebar-link", pathname.startsWith(item.href) && "sidebar-link-active")}
          >
            {item.icon}
            {item.label}
          </Link>
        ))}
      </nav>

      {/* User footer */}
      <div className="px-3 py-4 border-t border-slate-700">
        <div className="flex items-center gap-3 px-2 mb-3">
          <div className="w-8 h-8 rounded-full bg-primary-600 flex items-center justify-center text-white text-xs font-bold">
            {user ? getInitials(`${user.firstName} ${user.lastName}`) : "?"}
          </div>
          <div className="flex-1 min-w-0">
            <p className="text-white text-xs font-medium truncate">
              {user ? `${user.firstName} ${user.lastName}` : "User"}
            </p>
            <p className="text-slate-400 text-xs truncate">{user?.email}</p>
          </div>
        </div>
        <button
          onClick={logout}
          className="flex items-center gap-2 w-full px-3 py-2 text-slate-300 hover:text-red-400 hover:bg-slate-700 rounded-lg text-sm transition-colors"
        >
          <LogOut className="w-4 h-4" />
          Sign Out
        </button>
      </div>
    </aside>
  );
}

