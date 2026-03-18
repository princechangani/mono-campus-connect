import api from "../api";

interface LeaveApplication {
  id?: number;
  leavePublicId?: string;
  leaveType: string;
  fromDate: string;
  toDate: string;
  reason: string;
  documentUrl?: string;
  status?: string;
  reviewedBy?: number;
  reviewerRemarks?: string;
  reviewedAt?: string;
  applicantUserId?: number;
}

const ENDPOINT = "/leave-applications";

export const leaveApplicationService = {
  create: (data: LeaveApplication) =>
    api.post<LeaveApplication>(ENDPOINT, data),
  getAll: () => api.get<LeaveApplication[]>(ENDPOINT),
  getById: (id: number) => api.get<LeaveApplication>(`${ENDPOINT}/${id}`),
  getByApplicantUserId: (userId: number) =>
    api.get<LeaveApplication[]>(`${ENDPOINT}/user/${userId}`),
  getByApprovalStatus: (status: string) =>
    api.get<LeaveApplication[]>(`${ENDPOINT}/status/${status}`),
  update: (id: number, data: LeaveApplication) =>
    api.put<LeaveApplication>(`${ENDPOINT}/${id}`, data),
  delete: (id: number) => api.delete<void>(`${ENDPOINT}/${id}`),
};

