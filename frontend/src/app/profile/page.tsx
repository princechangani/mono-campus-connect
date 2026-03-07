"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import api from "@/lib/api";
import { getUser } from "@/lib/auth";
import { useForm } from "react-hook-form";
import toast from "react-hot-toast";
import { User, Lock, Trash2 } from "lucide-react";
import { getInitials } from "@/lib/utils";

export default function ProfilePage() {
  const user = getUser();
  const [tab, setTab]       = useState<"profile" | "password">("profile");
  const [saving, setSaving] = useState(false);

  const { register: regProfile, handleSubmit: handleProfile, reset } = useForm({
    defaultValues: {
      firstName: user?.firstName ?? "",
      lastName:  user?.lastName  ?? "",
      phoneNumber: user?.phoneNumber ?? "",
      address:   user?.address   ?? "",
      department: user?.department ?? "",
      semester:  user?.semester  ?? "",
    },
  });

  const { register: regPwd, handleSubmit: handlePwd, reset: resetPwd } = useForm<{
    currentPassword: string; newPassword: string; confirmPassword: string;
  }>();

  const onSaveProfile = async (data: any) => {
    setSaving(true);
    const formData = new FormData();
    Object.entries(data).forEach(([k, v]) => { if (v) formData.append(k, v as string); });
    try {
      await api.put("/profile", formData, { headers: { "Content-Type": "multipart/form-data" } });
      toast.success("Profile updated!");
    } catch {
      toast.error("Failed to update profile");
    } finally {
      setSaving(false);
    }
  };

  const onChangePwd = async (data: any) => {
    if (data.newPassword !== data.confirmPassword) {
      toast.error("Passwords do not match"); return;
    }
    setSaving(true);
    try {
      await api.put(`/profile/password?currentPassword=${data.currentPassword}&newPassword=${data.newPassword}`);
      toast.success("Password changed!");
      resetPwd();
    } catch {
      toast.error("Current password is incorrect");
    } finally {
      setSaving(false);
    }
  };

  return (
    <DashboardLayout title="My Profile">
      <div className="max-w-2xl mx-auto">
        {/* Avatar */}
        <div className="card mb-6 flex items-center gap-4">
          <div className="w-16 h-16 rounded-full bg-primary-600 flex items-center justify-center text-white text-xl font-bold flex-shrink-0">
            {user ? getInitials(`${user.firstName} ${user.lastName}`) : "?"}
          </div>
          <div>
            <h2 className="text-lg font-semibold text-gray-800">{user?.firstName} {user?.lastName}</h2>
            <p className="text-gray-500 text-sm">{user?.email}</p>
            <span className="badge bg-primary-100 text-primary-700 mt-1">{user?.role}</span>
          </div>
        </div>

        {/* Tabs */}
        <div className="flex gap-1 mb-6 bg-gray-100 p-1 rounded-lg w-fit">
          {(["profile", "password"] as const).map((t) => (
            <button key={t} onClick={() => setTab(t)}
              className={`px-4 py-1.5 rounded-md text-sm font-medium transition-all capitalize ${tab === t ? "bg-white shadow text-gray-800" : "text-gray-500 hover:text-gray-700"}`}>
              {t}
            </button>
          ))}
        </div>

        {tab === "profile" && (
          <form onSubmit={handleProfile(onSaveProfile)} className="card space-y-4">
            <h3 className="font-semibold text-gray-800 flex items-center gap-2"><User className="w-4 h-4" /> Personal Info</h3>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">First Name</label>
                <input {...regProfile("firstName")} className="input" />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Last Name</label>
                <input {...regProfile("lastName")} className="input" />
              </div>
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Phone Number</label>
              <input {...regProfile("phoneNumber")} className="input" placeholder="+91 XXXXX XXXXX" />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Address</label>
              <input {...regProfile("address")} className="input" />
            </div>
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Department</label>
                <input {...regProfile("department")} className="input" />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Semester</label>
                <input {...regProfile("semester")} className="input" />
              </div>
            </div>
            <div className="flex justify-between items-center pt-2">
              <button type="button" onClick={() => {
                api.delete("/profile/picture").then(() => toast.success("Picture removed")).catch(() => {});
              }} className="text-sm text-red-500 hover:underline flex items-center gap-1">
                <Trash2 className="w-3.5 h-3.5" /> Remove picture
              </button>
              <button type="submit" disabled={saving} className="btn-primary">{saving ? "Saving..." : "Save Changes"}</button>
            </div>
          </form>
        )}

        {tab === "password" && (
          <form onSubmit={handlePwd(onChangePwd)} className="card space-y-4">
            <h3 className="font-semibold text-gray-800 flex items-center gap-2"><Lock className="w-4 h-4" /> Change Password</h3>
            {(["currentPassword", "newPassword", "confirmPassword"] as const).map((field) => (
              <div key={field}>
                <label className="block text-sm font-medium text-gray-700 mb-1 capitalize">
                  {field.replace(/([A-Z])/g, " $1")}
                </label>
                <input {...regPwd(field, { required: true })} type="password" className="input" />
              </div>
            ))}
            <div className="flex justify-end pt-2">
              <button type="submit" disabled={saving} className="btn-primary">{saving ? "Changing..." : "Change Password"}</button>
            </div>
          </form>
        )}
      </div>
    </DashboardLayout>
  );
}

