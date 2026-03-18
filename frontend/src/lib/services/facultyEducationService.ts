import api from "../api";

interface FacultyEducation {
  id?: number;
  facultyId: number;
  degree: string;
  fieldOfStudy: string;
  institution: string;
  passingYear?: string;
  gradeOrPercentage?: string;
}

const ENDPOINT = "/faculty-education";

export const facultyEducationService = {
  create: (data: FacultyEducation) => api.post<FacultyEducation>(ENDPOINT, data),
  getAll: () => api.get<FacultyEducation[]>(ENDPOINT),
  getById: (id: number) => api.get<FacultyEducation>(`${ENDPOINT}/${id}`),
  getByFacultyId: (facultyId: number) =>
    api.get<FacultyEducation[]>(`${ENDPOINT}/faculty/${facultyId}`),
  update: (id: number, data: FacultyEducation) =>
    api.put<FacultyEducation>(`${ENDPOINT}/${id}`, data),
  delete: (id: number) => api.delete<void>(`${ENDPOINT}/${id}`),
};

