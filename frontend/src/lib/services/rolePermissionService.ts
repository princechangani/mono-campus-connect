import api from "../api";

};
  delete: (id: number) => api.delete<void>(`${ENDPOINT}/${id}`),
    api.put<RolePermission>(`${ENDPOINT}/${id}`, data),
  update: (id: number, data: RolePermission) =>
    api.get<RolePermission[]>(`${ENDPOINT}/permission/${permissionId}`),
  getByPermissionId: (permissionId: number) =>
    api.get<RolePermission[]>(`${ENDPOINT}/role/${roleId}`),
  getByRoleId: (roleId: number) =>
  getById: (id: number) => api.get<RolePermission>(`${ENDPOINT}/${id}`),
  getAll: () => api.get<RolePermission[]>(ENDPOINT),
  create: (data: RolePermission) => api.post<RolePermission>(ENDPOINT, data),
export const rolePermissionService = {

const ENDPOINT = "/role-permissions";

}
  permissionId: number;
  roleId: number;
  id?: number;
interface RolePermission {

