import api from "../api";

interface FeePayment {
  id?: number;
  studentId: number;
  amountPaid?: number;
  paymentDate?: string;
  paymentMode?: string;
  status?: string;
  transactionReference?: string;
}

const ENDPOINT = "/fee-payments";

export const feePaymentService = {
  create: (data: FeePayment) => api.post<FeePayment>(ENDPOINT, data),
  getAll: () => api.get<FeePayment[]>(ENDPOINT),
  getById: (id: number) => api.get<FeePayment>(`${ENDPOINT}/${id}`),
  getByStudentId: (studentId: number) =>
    api.get<FeePayment[]>(`${ENDPOINT}/student/${studentId}`),
  getByStatus: (status: string) =>
    api.get<FeePayment[]>(`${ENDPOINT}/status/${status}`),
  update: (id: number, data: FeePayment) =>
    api.put<FeePayment>(`${ENDPOINT}/${id}`, data),
  delete: (id: number) => api.delete<void>(`${ENDPOINT}/${id}`),
};

