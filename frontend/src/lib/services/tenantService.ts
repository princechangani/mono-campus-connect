import api from "../api";

interface Tenant {
  id?: string;
  name: string;
  code: string;
  contactEmail?: string;
  contactPhone?: string;
  address?: string;
  timezone?: string;
  logoUrl?: string;
  subscriptionPlan?: string;
  enabled?: boolean;
  createdAt?: string;
  // Stats
  totalUsers?: number;
  totalStudents?: number;
  totalFaculty?: number;
}

const ENDPOINT = "/tenants";

export const tenantService = {
  create: (data: Tenant) => api.post<Tenant>(ENDPOINT, data),
  getAll: () => api.get<Tenant[]>(ENDPOINT),
  getById: (id: string) => api.get<Tenant>(`${ENDPOINT}/${id}`),
  update: (id: string, data: Tenant) => api.put<Tenant>(`${ENDPOINT}/${id}`, data),
  enable: (id: string) => api.put<Tenant>(`${ENDPOINT}/${id}/enable`),
  disable: (id: string) => api.put<Tenant>(`${ENDPOINT}/${id}/disable`),
};
