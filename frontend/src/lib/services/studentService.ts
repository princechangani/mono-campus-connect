import api from "../api";

interface Student {
  id?: number;
  studentPublicId?: string;
  userId: number;
  batchId: number;
  enrollmentNumber: string;
  enrollmentDate?: string;
  currentSemester?: number;
  status?: string;
  tenantId?: string;
}

const ENDPOINT = "/students";

export const studentService = {
  create: (data: Student) => api.post<Student>(ENDPOINT, data),
  getAll: () => api.get<Student[]>(ENDPOINT),
  getById: (id: number) => api.get<Student>(`${ENDPOINT}/${id}`),
  getByPublicId: (publicId: string) =>
    api.get<Student>(`${ENDPOINT}/public-id/${publicId}`),
  getByBatchId: (batchId: number) =>
    api.get<Student[]>(`${ENDPOINT}/batch/${batchId}`),
  getByStatus: (status: string) =>
    api.get<Student[]>(`${ENDPOINT}/status/${status}`),
  update: (id: number, data: Student) =>
    api.put<Student>(`${ENDPOINT}/${id}`, data),
  delete: (id: number) => api.delete<void>(`${ENDPOINT}/${id}`),
};

