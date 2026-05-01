"use client";
import { useEffect, useState } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import api from "@/lib/api";
import { formatDate } from "@/lib/utils";
import { BookOpen, Download } from "lucide-react";

const typeIcons: Record<string, string> = { PDF:"📄", DOC:"📝", PPT:"📊", VIDEO:"🎬", LINK:"🔗" };

export default function StudentMaterialsPage() {
  const [materials, setMaterials] = useState<any[]>([]);
  const [courses,   setCourses]   = useState<any[]>([]);
  const [selected,  setSelected]  = useState("");
  const [loading,   setLoading]   = useState(true);

  useEffect(() => {
    api.get("/courses").then(r => setCourses(r.data)).catch(() => {});
  }, []);

  useEffect(() => {
    setLoading(true);
    const url = selected ? `/materials/course/${selected}` : "/materials";
    api.get(url).then(r => setMaterials(r.data)).catch(() => {}).finally(() => setLoading(false));
  }, [selected]);

  return (
    <DashboardLayout title="Study Materials">
      <div className="flex items-center justify-between mb-6 flex-wrap gap-3">
        <h2 className="text-xl font-semibold text-gray-800">Study Materials</h2>
        <select className="input w-48" value={selected} onChange={e => setSelected(e.target.value)}>
          <option value="">All Courses</option>
          {courses.map(c => <option key={c.id} value={c.courseCode}>{c.courseCode} – {c.courseName}</option>)}
        </select>
      </div>

      {loading ? (
        <div className="flex justify-center py-12"><div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600" /></div>
      ) : materials.length === 0 ? (
        <div className="card text-center py-12">
          <BookOpen className="w-10 h-10 text-gray-300 mx-auto mb-3" />
          <p className="text-gray-400">No materials found</p>
        </div>
      ) : (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {materials.map(m => (
            <div key={m.id} className="card hover:shadow-md transition-shadow">
              <div className="flex items-start gap-3">
                <div className="w-10 h-10 bg-primary-50 rounded-lg flex items-center justify-center text-lg flex-shrink-0">
                  {typeIcons[m.type] ?? "📄"}
                </div>
                <div className="flex-1 min-w-0">
                  <h3 className="font-medium text-gray-800 truncate">{m.title}</h3>
                  <p className="text-xs text-gray-500 mt-0.5">{m.courseCode}</p>
                  <p className="text-xs text-gray-400 mt-2 line-clamp-2">{m.description}</p>
                </div>
              </div>
              <div className="flex items-center justify-between mt-3 pt-3 border-t border-gray-100 text-xs text-gray-400">
                <span>{formatDate(m.uploadedDate)}</span>
                <span className="flex items-center gap-1"><Download className="w-3 h-3" />{m.downloadCount}</span>
              </div>
            </div>
          ))}
        </div>
      )}
    </DashboardLayout>
  );
}

