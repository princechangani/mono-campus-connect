import api from "../api";

interface FacultyWorkExperience {
  id?: number;
  facultyId: number;
  organization: string;
  role: string;
  fromDate?: string;
  toDate?: string;
  description?: string;
}

const ENDPOINT = "/faculty-work-experience";

export const facultyWorkExperienceService = {
  create: (data: FacultyWorkExperience) =>
    api.post<FacultyWorkExperience>(ENDPOINT, data),
  getAll: () => api.get<FacultyWorkExperience[]>(ENDPOINT),
  getById: (id: number) => api.get<FacultyWorkExperience>(`${ENDPOINT}/${id}`),
  getByFacultyId: (facultyId: number) =>
    api.get<FacultyWorkExperience[]>(`${ENDPOINT}/faculty/${facultyId}`),
  update: (id: number, data: FacultyWorkExperience) =>
    api.put<FacultyWorkExperience>(`${ENDPOINT}/${id}`, data),
  delete: (id: number) => api.delete<void>(`${ENDPOINT}/${id}`),
};

