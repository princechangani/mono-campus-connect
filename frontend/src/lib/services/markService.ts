import api from "../api";

interface Mark {
  id?: number;
  studentId: number;
  examId: number;
  marksObtained?: number;
}

const ENDPOINT = "/marks";

export const markService = {
  create: (data: Mark) => api.post<Mark>(ENDPOINT, data),
  getAll: () => api.get<Mark[]>(ENDPOINT),
  getById: (id: number) => api.get<Mark>(`${ENDPOINT}/${id}`),
  getByStudentId: (studentId: number) =>
    api.get<Mark[]>(`${ENDPOINT}/student/${studentId}`),
  getByExamId: (examId: number) =>
    api.get<Mark[]>(`${ENDPOINT}/exam/${examId}`),
  update: (id: number, data: Mark) => api.put<Mark>(`${ENDPOINT}/${id}`, data),
  delete: (id: number) => api.delete<void>(`${ENDPOINT}/${id}`),
};

