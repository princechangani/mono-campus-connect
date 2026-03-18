import api from "../api";
import { AcademicYear } from "@/types/models";

const ENDPOINT = "/academic-years";

export const academicYearService = {
  create: (data: AcademicYear) => api.post<AcademicYear>(ENDPOINT, data),
  getAll: () => api.get<AcademicYear[]>(ENDPOINT),
  getById: (id: number) => api.get<AcademicYear>(`${ENDPOINT}/${id}`),
  getByPublicId: (publicId: string) => api.get<AcademicYear>(`${ENDPOINT}/public-id/${publicId}`),
  getCurrentAcademicYear: () => api.get<AcademicYear>(`${ENDPOINT}/current`),
  getByLabel: (label: string) => api.get<AcademicYear>(`${ENDPOINT}/label/${label}`),
  update: (id: number, data: AcademicYear) => api.put<AcademicYear>(`${ENDPOINT}/${id}`, data),
  delete: (id: number) => api.delete<void>(`${ENDPOINT}/${id}`),
};

