import api from "../api";

interface RolePermission {
  id?: number;
  roleId: number;
  permissionId: number;
}

const ENDPOINT = "/role-permissions";

export const rolePermissionService = {
  create: (data: RolePermission) => api.post<RolePermission>(ENDPOINT, data),
  getAll: () => api.get<RolePermission[]>(ENDPOINT),
  getById: (id: number) => api.get<RolePermission>(`${ENDPOINT}/${id}`),
  getByRoleId: (roleId: number) => api.get<RolePermission[]>(`${ENDPOINT}/role/${roleId}`),
  getByPermissionId: (permissionId: number) => api.get<RolePermission[]>(`${ENDPOINT}/permission/${permissionId}`),
  update: (id: number, data: RolePermission) => api.put<RolePermission>(`${ENDPOINT}/${id}`, data),
  delete: (id: number) => api.delete<void>(`${ENDPOINT}/${id}`)
};
