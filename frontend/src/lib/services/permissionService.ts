import api from "../api";

interface Permission {
  id?: number;
  module: string;
  action: string;
  resource?: string;
}

const ENDPOINT = "/permissions";

export const permissionService = {
  create: (data: Permission) => api.post<Permission>(ENDPOINT, data),
  getAll: () => api.get<Permission[]>(ENDPOINT),
  getById: (id: number) => api.get<Permission>(`${ENDPOINT}/${id}`),
  getByModuleAndAction: (module: string, action: string) =>
    api.get<Permission>(`${ENDPOINT}/module/${module}/action/${action}`),
  update: (id: number, data: Permission) =>
    api.put<Permission>(`${ENDPOINT}/${id}`, data),
  delete: (id: number) => api.delete<void>(`${ENDPOINT}/${id}`),
};

