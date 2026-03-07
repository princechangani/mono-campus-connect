"use client";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import toast from "react-hot-toast";
import Link from "next/link";
import api from "@/lib/api";
import { GraduationCap, ArrowLeft } from "lucide-react";

const emailSchema = z.object({ email: z.string().email("Enter a valid email") });
const otpSchema   = z.object({ otp: z.string().min(6, "Enter the 6-digit OTP") });
const pwdSchema   = z.object({
  newPassword:     z.string().min(8, "Min 8 characters"),
  confirmPassword: z.string(),
}).refine(d => d.newPassword === d.confirmPassword, { message: "Passwords don't match", path: ["confirmPassword"] });

type Step = "email" | "otp" | "reset" | "done";

export default function ForgotPasswordPage() {
  const [step,  setStep]  = useState<Step>("email");
  const [email, setEmail] = useState("");
  const [otp,   setOtp]   = useState("");

  const emailForm = useForm({ resolver: zodResolver(emailSchema) });
  const otpForm   = useForm({ resolver: zodResolver(otpSchema) });
  const pwdForm   = useForm({ resolver: zodResolver(pwdSchema) });

  const sendOtp = async (data: any) => {
    try {
      await api.post("/otp/send", { email: data.email });
      setEmail(data.email);
      toast.success("OTP sent to your email");
      setStep("otp");
    } catch { toast.error("Email not found"); }
  };

  const verifyOtp = async (data: any) => {
    try {
      await api.post("/otp/verify", { email, otp: data.otp });
      setOtp(data.otp);
      setStep("reset");
    } catch { toast.error("Invalid or expired OTP"); }
  };

  const resetPassword = async (data: any) => {
    try {
      await api.post("/otp/reset-password", { email, otp, newPassword: data.newPassword });
      toast.success("Password reset successfully!");
      setStep("done");
    } catch { toast.error("Failed to reset password"); }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-primary-600 to-primary-900 flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-16 h-16 bg-white/20 rounded-2xl mb-4">
            <GraduationCap className="w-8 h-8 text-white" />
          </div>
          <h1 className="text-2xl font-bold text-white">Reset Password</h1>
        </div>

        <div className="card shadow-xl">
          {/* Step indicator */}
          <div className="flex items-center gap-2 mb-6">
            {(["email", "otp", "reset"] as const).map((s, i) => (
              <div key={s} className="flex items-center gap-2 flex-1">
                <div className={`w-6 h-6 rounded-full flex items-center justify-center text-xs font-bold transition-colors ${
                  step === s ? "bg-primary-600 text-white" :
                  ["email","otp","reset","done"].indexOf(step) > i ? "bg-green-500 text-white" : "bg-gray-200 text-gray-500"
                }`}>{i + 1}</div>
                {i < 2 && <div className={`flex-1 h-0.5 ${["email","otp","reset","done"].indexOf(step) > i ? "bg-green-400" : "bg-gray-200"}`} />}
              </div>
            ))}
          </div>

          {step === "email" && (
            <form onSubmit={emailForm.handleSubmit(sendOtp)} className="space-y-4">
              <p className="text-sm text-gray-600">Enter your registered email to receive an OTP.</p>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                <input {...emailForm.register("email")} type="email" placeholder="you@college.edu" className="input" />
                {emailForm.formState.errors.email && <p className="text-red-500 text-xs mt-1">{emailForm.formState.errors.email.message as string}</p>}
              </div>
              <button type="submit" disabled={emailForm.formState.isSubmitting} className="btn-primary w-full">
                {emailForm.formState.isSubmitting ? "Sending..." : "Send OTP"}
              </button>
            </form>
          )}

          {step === "otp" && (
            <form onSubmit={otpForm.handleSubmit(verifyOtp)} className="space-y-4">
              <p className="text-sm text-gray-600">Enter the 6-digit OTP sent to <strong>{email}</strong></p>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">OTP</label>
                <input {...otpForm.register("otp")} placeholder="123456" className="input text-center text-xl tracking-widest" maxLength={6} />
                {otpForm.formState.errors.otp && <p className="text-red-500 text-xs mt-1">{otpForm.formState.errors.otp.message as string}</p>}
              </div>
              <button type="submit" disabled={otpForm.formState.isSubmitting} className="btn-primary w-full">
                {otpForm.formState.isSubmitting ? "Verifying..." : "Verify OTP"}
              </button>
              <button type="button" onClick={() => api.post("/otp/resend", { email }).then(() => toast.success("OTP resent")).catch(() => {})}
                className="text-sm text-primary-600 hover:underline w-full text-center">
                Resend OTP
              </button>
            </form>
          )}

          {step === "reset" && (
            <form onSubmit={pwdForm.handleSubmit(resetPassword)} className="space-y-4">
              <p className="text-sm text-gray-600">Enter your new password.</p>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">New Password</label>
                <input {...pwdForm.register("newPassword")} type="password" className="input" />
                {pwdForm.formState.errors.newPassword && <p className="text-red-500 text-xs mt-1">{pwdForm.formState.errors.newPassword.message as string}</p>}
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Confirm Password</label>
                <input {...pwdForm.register("confirmPassword")} type="password" className="input" />
                {pwdForm.formState.errors.confirmPassword && <p className="text-red-500 text-xs mt-1">{pwdForm.formState.errors.confirmPassword.message as string}</p>}
              </div>
              <button type="submit" disabled={pwdForm.formState.isSubmitting} className="btn-primary w-full">
                {pwdForm.formState.isSubmitting ? "Resetting..." : "Reset Password"}
              </button>
            </form>
          )}

          {step === "done" && (
            <div className="text-center py-4">
              <div className="w-14 h-14 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <span className="text-2xl">✅</span>
              </div>
              <h3 className="font-semibold text-gray-800">Password Reset!</h3>
              <p className="text-gray-500 text-sm mt-1">You can now login with your new password.</p>
              <Link href="/login" className="btn-primary inline-block mt-4">Go to Login</Link>
            </div>
          )}

          {step !== "done" && (
            <Link href="/login" className="flex items-center gap-1 text-sm text-gray-500 hover:text-gray-700 mt-4">
              <ArrowLeft className="w-4 h-4" /> Back to login
            </Link>
          )}
        </div>
      </div>
    </div>
  );
}

