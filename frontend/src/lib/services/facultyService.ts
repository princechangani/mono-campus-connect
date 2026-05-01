import api from "../api";

interface Faculty {
  id?: number;
  facultyPublicId?: string;
  userId: number;
  departmentId: number;
  designation: string;
  employeeId: string;
  employmentType?: string;
  tenantId?: string;
}

const ENDPOINT = "/faculty";

export const facultyService = {
  create: (data: Faculty) => api.post<Faculty>(ENDPOINT, data),
  getAll: () => api.get<Faculty[]>(ENDPOINT),
  getById: (id: number) => api.get<Faculty>(`${ENDPOINT}/${id}`),
  getByPublicId: (publicId: string) =>
    api.get<Faculty>(`${ENDPOINT}/public-id/${publicId}`),
  getByDepartmentId: (departmentId: number) =>
    api.get<Faculty[]>(`${ENDPOINT}/department/${departmentId}`),
  update: (id: number, data: Faculty) =>
    api.put<Faculty>(`${ENDPOINT}/${id}`, data),
  delete: (id: number) => api.delete<void>(`${ENDPOINT}/${id}`),
};

