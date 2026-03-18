import api from "../api";

interface CourseAssignment {
  id?: number;
  courseId: number;
  facultyId: number;
  batchId: number;
  academicYearId?: number;
  semesterNumber?: number;
  section?: string;
}

const ENDPOINT = "/course-assignments";

export const courseAssignmentService = {
  create: (data: CourseAssignment) => api.post<CourseAssignment>(ENDPOINT, data),
  getAll: () => api.get<CourseAssignment[]>(ENDPOINT),
  getById: (id: number) => api.get<CourseAssignment>(`${ENDPOINT}/${id}`),
  getByFacultyId: (facultyId: number) =>
    api.get<CourseAssignment[]>(`${ENDPOINT}/faculty/${facultyId}`),
  getByCourseId: (courseId: number) =>
    api.get<CourseAssignment[]>(`${ENDPOINT}/course/${courseId}`),
  update: (id: number, data: CourseAssignment) =>
    api.put<CourseAssignment>(`${ENDPOINT}/${id}`, data),
  delete: (id: number) => api.delete<void>(`${ENDPOINT}/${id}`),
};

