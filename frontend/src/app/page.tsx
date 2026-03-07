"use client";
import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { getToken, getRole } from "@/lib/auth";

export default function HomePage() {
  const router = useRouter();

  useEffect(() => {
    const token = getToken();
    const role  = getRole();
    if (!token) {
      router.replace("/login");
      return;
    }
    switch (role) {
      case "SUPER_ADMIN": router.replace("/super-admin/dashboard"); break;
      case "ADMIN":       router.replace("/admin/dashboard");       break;
      case "FACULTY":     router.replace("/faculty/dashboard");     break;
      case "STUDENT":     router.replace("/student/dashboard");     break;
      default:            router.replace("/login");
    }
  }, [router]);

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <div className="animate-spin rounded-full h-10 w-10 border-b-2 border-primary-600" />
    </div>
  );
}

