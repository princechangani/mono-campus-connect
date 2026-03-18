import api from "../api";

interface FeeInvoice {
  id?: number;
  studentId: number;
  feeStructureId?: number;
  issueDate?: string;
  dueDate?: string;
  status?: string;
}

const ENDPOINT = "/fee-invoices";

export const feeInvoiceService = {
  create: (data: FeeInvoice) => api.post<FeeInvoice>(ENDPOINT, data),
  getAll: () => api.get<FeeInvoice[]>(ENDPOINT),
  getById: (id: number) => api.get<FeeInvoice>(`${ENDPOINT}/${id}`),
  getByStudentId: (studentId: number) =>
    api.get<FeeInvoice[]>(`${ENDPOINT}/student/${studentId}`),
  getByStatus: (status: string) =>
    api.get<FeeInvoice[]>(`${ENDPOINT}/status/${status}`),
  update: (id: number, data: FeeInvoice) =>
    api.put<FeeInvoice>(`${ENDPOINT}/${id}`, data),
  delete: (id: number) => api.delete<void>(`${ENDPOINT}/${id}`),
};

