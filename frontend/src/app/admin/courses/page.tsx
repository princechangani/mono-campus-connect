"use client";
import { useEffect, useState, useCallback } from "react";
import DashboardLayout from "@/components/layout/DashboardLayout";
import api from "@/lib/api";
import toast from "react-hot-toast";
import { Plus, Pencil, Trash2, X, BookOpen, SlidersHorizontal, RotateCcw } from "lucide-react";
import { useForm } from "react-hook-form";

const CATEGORIES   = ["Core", "Elective", "Lab", "Project", "Seminar"];
const CREDIT_OPTS  = [1, 2, 3, 4, 5, 6];

const TYPE_BADGE: Record<string, string> = {
  COMPULSORY: "bg-blue-100 text-blue-700",
  OPTIONAL:   "bg-amber-100 text-amber-700",
};

const CAT_BADGE: Record<string, string> = {
  Core:     "bg-green-100 text-green-700",
  Elective: "bg-purple-100 text-purple-700",
  Lab:      "bg-cyan-100 text-cyan-700",
  Project:  "bg-rose-100 text-rose-700",
  Seminar:  "bg-orange-100 text-orange-700",
};

interface Filters {
  search:      string;
  semester:    string;
  department:  string;
  instructor:  string;
  credits:     string;
  subjectType: string;
  category:    string;
}

const EMPTY_FILTERS: Filters = {
  search: "", semester: "", department: "", instructor: "",
  credits: "", subjectType: "", category: "",
};

export default function AdminCoursesPage() {
  const [courses,      setCourses]      = useState<any[]>([]);
  const [filtered,     setFiltered]     = useState<any[]>([]);
  const [filters,      setFilters]      = useState<Filters>(EMPTY_FILTERS);
  const [showFilters,  setShowFilters]  = useState(false);
  const [loading,      setLoading]      = useState(true);
  const [showModal,    setShowModal]    = useState(false);
  const [editing,      setEditing]      = useState<any>(null);

  // derived unique values for filter dropdowns
  const [semesters,   setSemesters]   = useState<string[]>([]);
  const [departments, setDepartments] = useState<string[]>([]);
  const [instructors, setInstructors] = useState<string[]>([]);

  const { register, handleSubmit, reset, formState: { isSubmitting } } = useForm<any>();

  const load = useCallback(() => {
    setLoading(true);
    api.get("/courses").then(r => {
      const data: any[] = r.data;
      setCourses(data);
      setSemesters([...new Set<string>(data.map(c => c.semester).filter(Boolean))].sort());
      setDepartments([...new Set<string>(data.map(c => c.department).filter(Boolean))].sort());
      setInstructors([...new Set<string>(data.map(c => c.instructor).filter(Boolean))].sort());
    }).catch(() => {}).finally(() => setLoading(false));
  }, []);

  useEffect(() => { load(); }, [load]);

  // apply all filters client-side (fast, no extra round-trips)
  useEffect(() => {
    let result = [...courses];
    const { search, semester, department, instructor, credits, subjectType, category } = filters;
    if (search)      result = result.filter(c => `${c.courseCode} ${c.courseName}`.toLowerCase().includes(search.toLowerCase()));
    if (semester)    result = result.filter(c => c.semester    === semester);
    if (department)  result = result.filter(c => c.department  === department);
    if (instructor)  result = result.filter(c => c.instructor  === instructor);
    if (credits)     result = result.filter(c => String(c.credits) === credits);
    if (subjectType) result = result.filter(c => c.subjectType === subjectType);
    if (category)    result = result.filter(c => c.category    === category);
    setFiltered(result);
  }, [courses, filters]);

  const setFilter = (key: keyof Filters, val: string) =>
    setFilters(prev => ({ ...prev, [key]: val }));

  const clearFilters = () => setFilters(EMPTY_FILTERS);
  const activeCount  = Object.entries(filters).filter(([k, v]) => k !== "search" && v !== "").length;

  const openCreate = () => { setEditing(null); reset({ subjectType: "COMPULSORY", category: "Core" }); setShowModal(true); };
  const openEdit   = (c: any) => { setEditing(c); reset(c); setShowModal(true); };

  const onSubmit = async (data: any) => {
    try {
      const payload = { ...data, credits: Number(data.credits) };
      editing ? await api.put(`/courses/${editing.id}`, payload) : await api.post("/courses", payload);
      toast.success(editing ? "Subject updated" : "Subject created");
      setShowModal(false); load();
    } catch { toast.error("Operation failed"); }
  };

  const deleteCourse = (id: number) => {
    if (!confirm("Delete this subject?")) return;
    api.delete(`/courses/${id}`)
      .then(() => { toast.success("Deleted"); load(); })
      .catch(() => toast.error("Delete failed"));
  };

  return (
    <DashboardLayout title="Subjects">
      {/* Header */}
      <div className="flex items-center justify-between mb-4 flex-wrap gap-3">
        <h2 className="text-xl font-semibold text-gray-800">
          Subjects
          <span className="ml-2 text-sm font-normal text-gray-400">
            {filtered.length} of {courses.length}
          </span>
        </h2>
        <div className="flex gap-2 flex-wrap">
          <input
            placeholder="Search code or name…"
            className="input w-52"
            value={filters.search}
            onChange={e => setFilter("search", e.target.value)}
          />
          <button
            onClick={() => setShowFilters(p => !p)}
            className={`btn-secondary flex items-center gap-2 relative ${showFilters ? "ring-2 ring-primary-400" : ""}`}
          >
            <SlidersHorizontal className="w-4 h-4" />
            Filters
            {activeCount > 0 && (
              <span className="absolute -top-1.5 -right-1.5 w-4 h-4 bg-primary-600 text-white text-[10px] rounded-full flex items-center justify-center">
                {activeCount}
              </span>
            )}
          </button>
          <button onClick={openCreate} className="btn-primary flex items-center gap-2">
            <Plus className="w-4 h-4" /> Add Subject
          </button>
        </div>
      </div>

      {/* Filter bar */}
      {showFilters && (
        <div className="card mb-5 border-primary-100">
          <div className="flex items-center justify-between mb-3">
            <p className="text-sm font-semibold text-gray-700">Filter Subjects</p>
            {activeCount > 0 && (
              <button onClick={clearFilters} className="flex items-center gap-1 text-xs text-red-500 hover:text-red-700">
                <RotateCcw className="w-3 h-3" /> Clear all
              </button>
            )}
          </div>
          <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-6 gap-3">
            {/* Semester */}
            <div>
              <label className="block text-xs font-medium text-gray-500 mb-1">Semester</label>
              <select className="input text-sm" value={filters.semester} onChange={e => setFilter("semester", e.target.value)}>
                <option value="">All</option>
                {semesters.map(s => <option key={s} value={s}>Sem {s}</option>)}
              </select>
            </div>
            {/* Department */}
            <div>
              <label className="block text-xs font-medium text-gray-500 mb-1">Department</label>
              <select className="input text-sm" value={filters.department} onChange={e => setFilter("department", e.target.value)}>
                <option value="">All</option>
                {departments.map(d => <option key={d} value={d}>{d}</option>)}
              </select>
            </div>
            {/* Faculty */}
            <div>
              <label className="block text-xs font-medium text-gray-500 mb-1">Faculty</label>
              <select className="input text-sm" value={filters.instructor} onChange={e => setFilter("instructor", e.target.value)}>
                <option value="">All</option>
                {instructors.map(i => <option key={i} value={i}>{i}</option>)}
              </select>
            </div>
            {/* Credits */}
            <div>
              <label className="block text-xs font-medium text-gray-500 mb-1">Credits</label>
              <select className="input text-sm" value={filters.credits} onChange={e => setFilter("credits", e.target.value)}>
                <option value="">Any</option>
                {CREDIT_OPTS.map(c => <option key={c} value={String(c)}>{c} cr</option>)}
              </select>
            </div>
            {/* Type */}
            <div>
              <label className="block text-xs font-medium text-gray-500 mb-1">Type</label>
              <select className="input text-sm" value={filters.subjectType} onChange={e => setFilter("subjectType", e.target.value)}>
                <option value="">All</option>
                <option value="COMPULSORY">Compulsory</option>
                <option value="OPTIONAL">Optional</option>
              </select>
            </div>
            {/* Category */}
            <div>
              <label className="block text-xs font-medium text-gray-500 mb-1">Category</label>
              <select className="input text-sm" value={filters.category} onChange={e => setFilter("category", e.target.value)}>
                <option value="">All</option>
                {CATEGORIES.map(c => <option key={c} value={c}>{c}</option>)}
              </select>
            </div>
          </div>

          {/* Active filter chips */}
          {activeCount > 0 && (
            <div className="flex flex-wrap gap-2 mt-3 pt-3 border-t border-gray-100">
              {Object.entries(filters)
                .filter(([k, v]) => k !== "search" && v !== "")
                .map(([k, v]) => (
                  <span key={k} className="inline-flex items-center gap-1 px-2 py-0.5 bg-primary-50 text-primary-700 text-xs rounded-full">
                    {k === "subjectType" ? (v === "COMPULSORY" ? "Compulsory" : "Optional") : v}
                    <button onClick={() => setFilter(k as keyof Filters, "")} className="ml-0.5 hover:text-red-500">
                      <X className="w-3 h-3" />
                    </button>
                  </span>
                ))}
            </div>
          )}
        </div>
      )}

      {/* Grid */}
      {loading ? (
        <div className="flex justify-center py-16">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600" />
        </div>
      ) : filtered.length === 0 ? (
        <div className="card text-center py-14">
          <BookOpen className="w-10 h-10 text-gray-300 mx-auto mb-3" />
          <p className="text-gray-400 font-medium">No subjects match your filters</p>
          {activeCount > 0 && (
            <button onClick={clearFilters} className="mt-2 text-sm text-primary-600 hover:underline">
              Clear filters
            </button>
          )}
        </div>
      ) : (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
          {filtered.map(c => (
            <div key={c.id} className="card hover:shadow-md transition-shadow !p-0 overflow-hidden">
              {/* Sem colour strip */}
              <div className={`h-1 ${
                c.semester === "2" ? "bg-green-400" :
                c.semester === "4" ? "bg-blue-400" :
                c.semester === "6" ? "bg-purple-400" : "bg-gray-300"
              }`} />
              <div className="p-4">
                {/* Badges row */}
                <div className="flex flex-wrap gap-1 mb-2">
                  <span className="badge bg-gray-100 text-gray-600 font-mono">{c.courseCode}</span>
                  <span className="badge bg-sky-50 text-sky-700">Sem {c.semester}</span>
                  {c.subjectType && (
                    <span className={`badge ${TYPE_BADGE[c.subjectType] ?? "bg-gray-100 text-gray-600"}`}>
                      {c.subjectType === "COMPULSORY" ? "Compulsory" : "Optional"}
                    </span>
                  )}
                  {c.category && (
                    <span className={`badge ${CAT_BADGE[c.category] ?? "bg-gray-100 text-gray-600"}`}>
                      {c.category}
                    </span>
                  )}
                </div>

                <h3 className="font-semibold text-gray-800 text-sm leading-snug">{c.courseName}</h3>
                <p className="text-xs text-gray-500 mt-0.5">{c.department}</p>

                <div className="flex items-center justify-between mt-3 pt-3 border-t border-gray-100">
                  <div className="text-xs text-gray-500">
                    <p>👨‍🏫 {c.instructor ?? "TBA"}</p>
                    <p className="mt-0.5">🎯 {c.credits} Credit{c.credits !== 1 ? "s" : ""}</p>
                  </div>
                  <div className="flex gap-1">
                    <button onClick={() => openEdit(c)} className="p-1.5 hover:bg-gray-100 rounded text-gray-400">
                      <Pencil className="w-3.5 h-3.5" />
                    </button>
                    <button onClick={() => deleteCourse(c.id)} className="p-1.5 hover:bg-red-50 rounded text-red-400">
                      <Trash2 className="w-3.5 h-3.5" />
                    </button>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Add/Edit Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl shadow-xl w-full max-w-lg max-h-[92vh] overflow-y-auto">
            <div className="flex items-center justify-between p-5 border-b sticky top-0 bg-white z-10">
              <h3 className="font-semibold text-gray-800">{editing ? "Edit Subject" : "Add Subject"}</h3>
              <button onClick={() => setShowModal(false)}><X className="w-5 h-5 text-gray-400" /></button>
            </div>
            <form onSubmit={handleSubmit(onSubmit)} className="p-5 space-y-3">
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Course Code *</label>
                  <input {...register("courseCode", { required: true })} className="input" placeholder="CS401" />
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Credits *</label>
                  <select {...register("credits", { required: true })} className="input">
                    {CREDIT_OPTS.map(c => <option key={c} value={c}>{c}</option>)}
                  </select>
                </div>
              </div>
              <div>
                <label className="block text-xs font-medium text-gray-700 mb-1">Subject Name *</label>
                <input {...register("courseName", { required: true })} className="input" />
              </div>
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Department</label>
                  <input {...register("department")} className="input" />
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Semester</label>
                  <input {...register("semester")} className="input" placeholder="4" />
                </div>
              </div>
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Instructor</label>
                  <input {...register("instructor")} className="input" />
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Faculty ID</label>
                  <input {...register("facultyId")} className="input" placeholder="FAC001" />
                </div>
              </div>
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Type</label>
                  <select {...register("subjectType")} className="input">
                    <option value="COMPULSORY">Compulsory</option>
                    <option value="OPTIONAL">Optional</option>
                  </select>
                </div>
                <div>
                  <label className="block text-xs font-medium text-gray-700 mb-1">Category</label>
                  <select {...register("category")} className="input">
                    <option value="">— Select —</option>
                    {CATEGORIES.map(c => <option key={c} value={c}>{c}</option>)}
                  </select>
                </div>
              </div>
              <div className="flex justify-end gap-2 pt-2">
                <button type="button" onClick={() => setShowModal(false)} className="btn-secondary text-sm">Cancel</button>
                <button type="submit" disabled={isSubmitting} className="btn-primary text-sm">
                  {isSubmitting ? "Saving…" : editing ? "Update" : "Create"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </DashboardLayout>
  );
}

