import api from "../api";

interface AuditLogDocument {
  id?: string;
  tenantId: string;
  actorUserId?: number;
  actorRole?: string;
  action: string;
  resourceType: string;
  resourceId?: string;
  changes?: Record<string, any>;
  createdAt?: string;
}

const ENDPOINT = "/documents/audit-logs";

export const auditLogDocumentService = {
  create: (data: AuditLogDocument) =>
    api.post<AuditLogDocument>(ENDPOINT, data),
  getAll: () => api.get<AuditLogDocument[]>(ENDPOINT),
  getById: (id: string) => api.get<AuditLogDocument>(`${ENDPOINT}/${id}`),
  getByTenantId: (tenantId: string) =>
    api.get<AuditLogDocument[]>(`${ENDPOINT}/tenant/${tenantId}`),
  getByResourceType: (resourceType: string) =>
    api.get<AuditLogDocument[]>(`${ENDPOINT}/resource-type/${resourceType}`),
  update: (id: string, data: AuditLogDocument) =>
    api.put<AuditLogDocument>(`${ENDPOINT}/${id}`, data),
  delete: (id: string) => api.delete<void>(`${ENDPOINT}/${id}`),
};

