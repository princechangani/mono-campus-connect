import api from "../api";

interface ExamSchedule {
  id?: number;
  examId: number;
  courseId?: number;
  roomId?: number;
  examDate: string;
  startTime?: string;
  endTime?: string;
  invigilatorId?: number;
}

const ENDPOINT = "/exam-schedules";

export const examScheduleService = {
  create: (data: ExamSchedule) => api.post<ExamSchedule>(ENDPOINT, data),
  getAll: () => api.get<ExamSchedule[]>(ENDPOINT),
  getById: (id: number) => api.get<ExamSchedule>(`${ENDPOINT}/${id}`),
  getByExamId: (examId: number) =>
    api.get<ExamSchedule[]>(`${ENDPOINT}/exam/${examId}`),
  getByRoomId: (roomId: number) =>
    api.get<ExamSchedule[]>(`${ENDPOINT}/room/${roomId}`),
  update: (id: number, data: ExamSchedule) =>
    api.put<ExamSchedule>(`${ENDPOINT}/${id}`, data),
  delete: (id: number) => api.delete<void>(`${ENDPOINT}/${id}`),
};

