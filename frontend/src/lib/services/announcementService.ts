import api from "../api";

interface Announcement {
  id?: number;
  title: string;
  content: string;
  audience?: string;
  priority?: string;
  pinnedUntil?: string;
  expiresAt?: string;
  publishedAt?: string;
  tenantId?: string;
}

const ENDPOINT = "/announcements";

export const announcementService = {
  create: (data: Announcement) => api.post<Announcement>(ENDPOINT, data),
  getAll: () => api.get<Announcement[]>(ENDPOINT),
  getById: (id: number) => api.get<Announcement>(`${ENDPOINT}/${id}`),
  getActive: () => api.get<Announcement[]>(`${ENDPOINT}/active`),
  getByTenantId: (tenantId: string) => api.get<Announcement[]>(`${ENDPOINT}/tenant/${tenantId}`),
  update: (id: number, data: Announcement) => api.put<Announcement>(`${ENDPOINT}/${id}`, data),
  delete: (id: number) => api.delete<void>(`${ENDPOINT}/${id}`)
};
