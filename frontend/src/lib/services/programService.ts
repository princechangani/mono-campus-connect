import api from "../api";

interface Program {
  id?: number;
  programPublicId?: string;
  name: string;
  code: string;
  departmentId: number;
  degreeLevel?: string;
  durationYears?: number;
  totalSemesters?: number;
  totalCredits?: number;
  isActive?: boolean;
  tenantId?: string;
}

const ENDPOINT = "/programs";

export const programService = {
  create: (data: Program) => api.post<Program>(ENDPOINT, data),
  getAll: () => api.get<Program[]>(ENDPOINT),
  getById: (id: number) => api.get<Program>(`${ENDPOINT}/${id}`),
  getByPublicId: (publicId: string) =>
    api.get<Program>(`${ENDPOINT}/public-id/${publicId}`),
  getByDepartmentId: (departmentId: number) =>
    api.get<Program[]>(`${ENDPOINT}/department/${departmentId}`),
  update: (id: number, data: Program) =>
    api.put<Program>(`${ENDPOINT}/${id}`, data),
  delete: (id: number) => api.delete<void>(`${ENDPOINT}/${id}`),
};

