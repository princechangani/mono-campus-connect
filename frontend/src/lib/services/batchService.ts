import api from "../api";

interface Batch {
  id?: number;
  batchPublicId?: string;
  name: string;
  programId: number;
  academicYearId?: number;
  currentSemester?: number;
  maxStudents?: number;
  classTeacherId?: number;
  tenantId?: string;
}

const ENDPOINT = "/batches";

export const batchService = {
  create: (data: Batch) => api.post<Batch>(ENDPOINT, data),
  getAll: () => api.get<Batch[]>(ENDPOINT),
  getById: (id: number) => api.get<Batch>(`${ENDPOINT}/${id}`),
  getByPublicId: (publicId: string) =>
    api.get<Batch>(`${ENDPOINT}/public-id/${publicId}`),
  getByProgramId: (programId: number) =>
    api.get<Batch[]>(`${ENDPOINT}/program/${programId}`),
  update: (id: number, data: Batch) =>
    api.put<Batch>(`${ENDPOINT}/${id}`, data),
  delete: (id: number) => api.delete<void>(`${ENDPOINT}/${id}`),
};

