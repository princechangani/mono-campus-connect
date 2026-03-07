import { type ClassValue, clsx } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export function formatDate(date: string | Date) {
  return new Date(date).toLocaleDateString("en-IN", {
    day: "2-digit", month: "short", year: "numeric",
  });
}

export function formatDateTime(date: string | Date) {
  return new Date(date).toLocaleString("en-IN", {
    day: "2-digit", month: "short", year: "numeric",
    hour: "2-digit", minute: "2-digit",
  });
}

export function getInitials(name: string) {
  return name
    .split(" ")
    .map((n) => n[0])
    .join("")
    .toUpperCase()
    .slice(0, 2);
}

export function gradeColor(grade: string) {
  const map: Record<string, string> = {
    "A+": "text-green-700 bg-green-100",
    "A":  "text-green-700 bg-green-100",
    "B":  "text-blue-700 bg-blue-100",
    "C":  "text-yellow-700 bg-yellow-100",
    "D":  "text-orange-700 bg-orange-100",
    "F":  "text-red-700 bg-red-100",
  };
  return map[grade] ?? "text-gray-700 bg-gray-100";
}

