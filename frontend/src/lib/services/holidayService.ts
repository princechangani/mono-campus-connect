import api from "../api";

interface Holiday {
  id?: number;
  title: string;
  holidayDate: string;
  holidayType: string;
  appliesTo?: string;
  description?: string;
  tenantId?: string;
}

const ENDPOINT = "/holidays";

export const holidayService = {
  create: (data: Holiday) => api.post<Holiday>(ENDPOINT, data),
  getAll: () => api.get<Holiday[]>(ENDPOINT),
  getById: (id: number) => api.get<Holiday>(`${ENDPOINT}/${id}`),
  getByTenantId: (tenantId: string) => api.get<Holiday[]>(`${ENDPOINT}/tenant/${tenantId}`),
  update: (id: number, data: Holiday) => api.put<Holiday>(`${ENDPOINT}/${id}`, data),
  delete: (id: number) => api.delete<void>(`${ENDPOINT}/${id}`),
};

