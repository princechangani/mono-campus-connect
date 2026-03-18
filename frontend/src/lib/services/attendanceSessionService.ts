import api from "../api";

interface AttendanceSession {
  id?: number;
  courseAssignmentId: number;
  slotId?: number;
  roomId?: number;
  sessionDate: string;
  topicCovered?: string;
  conductedBy?: number;
}

const ENDPOINT = "/attendance-sessions";

export const attendanceSessionService = {
  create: (data: AttendanceSession) => api.post<AttendanceSession>(ENDPOINT, data),
  getAll: () => api.get<AttendanceSession[]>(ENDPOINT),
  getById: (id: number) => api.get<AttendanceSession>(`${ENDPOINT}/${id}`),
  getByCourseAssignmentId: (courseAssignmentId: number) =>
    api.get<AttendanceSession[]>(`${ENDPOINT}/course-assignment/${courseAssignmentId}`),
  getByConductedBy: (facultyId: number) =>
    api.get<AttendanceSession[]>(`${ENDPOINT}/faculty/${facultyId}`),
  update: (id: number, data: AttendanceSession) =>
    api.put<AttendanceSession>(`${ENDPOINT}/${id}`, data),
  delete: (id: number) => api.delete<void>(`${ENDPOINT}/${id}`),
};

