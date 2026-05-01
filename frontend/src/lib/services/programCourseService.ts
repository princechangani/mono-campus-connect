import api from "../api";

interface ProgramCourse {
  id?: number;
  programId: number;
  courseId: number;
  semesterNumber?: number;
  isElective?: boolean;
  isMandatory?: boolean;
}

const ENDPOINT = "/program-courses";

export const programCourseService = {
  create: (data: ProgramCourse) => api.post<ProgramCourse>(ENDPOINT, data),
  getAll: () => api.get<ProgramCourse[]>(ENDPOINT),
  getById: (id: number) => api.get<ProgramCourse>(`${ENDPOINT}/${id}`),
  getByProgramId: (programId: number) =>
    api.get<ProgramCourse[]>(`${ENDPOINT}/program/${programId}`),
  getByCourseId: (courseId: number) =>
    api.get<ProgramCourse[]>(`${ENDPOINT}/course/${courseId}`),
  update: (id: number, data: ProgramCourse) =>
    api.put<ProgramCourse>(`${ENDPOINT}/${id}`, data),
  delete: (id: number) => api.delete<void>(`${ENDPOINT}/${id}`),
};

