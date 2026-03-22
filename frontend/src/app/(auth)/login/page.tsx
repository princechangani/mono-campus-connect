"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import toast from "react-hot-toast";
import Link from "next/link";
import api from "@/lib/api";
import { setToken, setRole, setUser, removeToken, removeRole, removeUser } from "@/lib/auth";
import { Eye, EyeOff, GraduationCap } from "lucide-react";

const schema = z.object({
  email:    z.string().email("Enter a valid email"),
  password: z.string().min(1, "Password is required"),
});
type FormData = z.infer<typeof schema>;

export default function LoginPage() {
  const router = useRouter();
  const [showPwd, setShowPwd] = useState(false);
  const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm<FormData>({
    resolver: zodResolver(schema),
  });

  const onSubmit = async (data: FormData) => {
    try {
      const res = await api.post("/auth/login", data);
      console.log("Login response:", res.data);
      const { token, role, user } = res.data;

      // clear any stale session first
      removeToken();
      removeRole();
      removeUser();

      setToken(token);
      setRole(role);
      if (user) setUser(user);

      toast.success("Welcome back!");
      switch (role) {
        case "SUPER_ADMIN": router.push("/super-admin/dashboard"); break;
        case "ADMIN":       router.push("/admin/dashboard");       break;
        case "FACULTY":     router.push("/faculty/dashboard");     break;
        default:            router.push("/student/dashboard");
      }
    } catch (err: any) {
      toast.error(err?.response?.data?.message ?? "Invalid credentials");
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-primary-600 to-primary-900 flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        {/* Logo */}
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-16 h-16 bg-white/20 rounded-2xl mb-4">
            <GraduationCap className="w-8 h-8 text-white" />
          </div>
          <h1 className="text-3xl font-bold text-white">CampusConnect</h1>
          <p className="text-primary-200 mt-1 text-sm">College Management System</p>
        </div>

        {/* Card */}
        <div className="card shadow-xl">
          <h2 className="text-xl font-semibold text-gray-800 mb-6">Sign in to your account</h2>

          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
              <input {...register("email")} type="email" placeholder="you@college.edu"
                className={`input ${errors.email ? "border-red-400 focus:ring-red-400" : ""}`} />
              {errors.email && <p className="text-red-500 text-xs mt-1">{errors.email.message}</p>}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Password</label>
              <div className="relative">
                <input {...register("password")} type={showPwd ? "text" : "password"}
                  placeholder="••••••••"
                  className={`input pr-10 ${errors.password ? "border-red-400 focus:ring-red-400" : ""}`} />
                <button type="button" onClick={() => setShowPwd(!showPwd)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600">
                  {showPwd ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                </button>
              </div>
              {errors.password && <p className="text-red-500 text-xs mt-1">{errors.password.message}</p>}
            </div>

            <div className="flex justify-end">
              <Link href="/forgot-password" className="text-sm text-primary-600 hover:underline">
                Forgot password?
              </Link>
            </div>

            <button type="submit" disabled={isSubmitting} className="btn-primary w-full py-2.5 text-sm">
              {isSubmitting ? "Signing in..." : "Sign In"}
            </button>
          </form>
        </div>

        <p className="text-center text-primary-200 text-sm mt-6">
          © {new Date().getFullYear()} CampusConnect. All rights reserved.
        </p>
      </div>
    </div>
  );
}

