import api from "../api";

};
  delete: (id: number) => api.delete<void>(`${ENDPOINT}/${id}`),
  update: (id: number, data: Announcement) => api.put<Announcement>(`${ENDPOINT}/${id}`, data),
  getByTenantId: (tenantId: string) => api.get<Announcement[]>(`${ENDPOINT}/tenant/${tenantId}`),
  getActive: () => api.get<Announcement[]>(`${ENDPOINT}/active`),
  getById: (id: number) => api.get<Announcement>(`${ENDPOINT}/${id}`),
  getAll: () => api.get<Announcement[]>(ENDPOINT),
  create: (data: Announcement) => api.post<Announcement>(ENDPOINT, data),
export const announcementService = {

const ENDPOINT = "/announcements";

}
  tenantId?: string;
  publishedAt?: string;
  expiresAt?: string;
  pinnedUntil?: string;
  priority?: string;
  audience?: string;
  content: string;
  title: string;
  id?: number;
interface Announcement {

