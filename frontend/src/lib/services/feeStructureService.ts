import api from "../api";

interface FeeStructure {
  id?: number;
  programId: number;
  semesterNumber?: number;
  academicYearId?: number;
  feeName: string;
  totalAmount: number;
  tenantId?: string;
}

const ENDPOINT = "/fee-structures";

export const feeStructureService = {
  create: (data: FeeStructure) => api.post<FeeStructure>(ENDPOINT, data),
  getAll: () => api.get<FeeStructure[]>(ENDPOINT),
  getById: (id: number) => api.get<FeeStructure>(`${ENDPOINT}/${id}`),
  getByProgramId: (programId: number) => api.get<FeeStructure[]>(`${ENDPOINT}/program/${programId}`),
  getByTenantId: (tenantId: string) => api.get<FeeStructure[]>(`${ENDPOINT}/tenant/${tenantId}`),
  update: (id: number, data: FeeStructure) => api.put<FeeStructure>(`${ENDPOINT}/${id}`, data),
  delete: (id: number) => api.delete<void>(`${ENDPOINT}/${id}`),
};

