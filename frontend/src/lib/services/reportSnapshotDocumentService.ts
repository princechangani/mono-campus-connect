import api from "../api";

interface ReportSnapshotDocument {
  id?: string;
  publicId?: string;
  tenantId: string;
  reportType: string;
  referenceType?: string;
  referenceId?: string;
  data?: Record<string, any>;
  generatedAt?: string;
  expiresAt?: string;
}

const ENDPOINT = "/documents/report-snapshots";

export const reportSnapshotDocumentService = {
  create: (data: ReportSnapshotDocument) =>
    api.post<ReportSnapshotDocument>(ENDPOINT, data),
  getAll: () => api.get<ReportSnapshotDocument[]>(ENDPOINT),
  getById: (id: string) =>
    api.get<ReportSnapshotDocument>(`${ENDPOINT}/${id}`),
  getByPublicId: (tenantId: string, publicId: string) =>
    api.get<ReportSnapshotDocument>(`${ENDPOINT}/public-id`, {
      params: { tenantId, publicId },
    }),
  getByTenantId: (tenantId: string) =>
    api.get<ReportSnapshotDocument[]>(`${ENDPOINT}/tenant/${tenantId}`),
  getByReportType: (reportType: string) =>
    api.get<ReportSnapshotDocument[]>(
      `${ENDPOINT}/report-type/${reportType}`
    ),
  update: (id: string, data: ReportSnapshotDocument) =>
    api.put<ReportSnapshotDocument>(`${ENDPOINT}/${id}`, data),
  delete: (id: string) => api.delete<void>(`${ENDPOINT}/${id}`),
};

