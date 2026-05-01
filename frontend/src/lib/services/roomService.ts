import api from "../api";

interface Room {
  id?: number;
  roomPublicId?: string;
  building: string;
  floor?: string;
  roomNumber: string;
  name: string;
  capacity: number;
  roomType: string;
}

const ENDPOINT = "/rooms";

export const roomService = {
  create: (data: Room) => api.post<Room>(ENDPOINT, data),
  getAll: () => api.get<Room[]>(ENDPOINT),
  getById: (id: number) => api.get<Room>(`${ENDPOINT}/${id}`),
  getByRoomNumber: (roomNumber: string) =>
    api.get<Room>(`${ENDPOINT}/room-number/${roomNumber}`),
  getByBuilding: (building: string) =>
    api.get<Room[]>(`${ENDPOINT}/building/${building}`),
  getByRoomType: (roomType: string) =>
    api.get<Room[]>(`${ENDPOINT}/type/${roomType}`),
  update: (id: number, data: Room) => api.put<Room>(`${ENDPOINT}/${id}`, data),
  delete: (id: number) => api.delete<void>(`${ENDPOINT}/${id}`),
};

