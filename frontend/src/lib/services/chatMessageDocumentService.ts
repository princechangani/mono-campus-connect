import api from "../api";

interface ChatMessageDocument {
  id?: string;
  publicId?: string;
  tenantId: string;
  channelId: string;
  senderId: number;
  text: string;
  attachments?: string[];
  reactions?: Record<string, any>;
  readBy?: number[];
  messageType?: string;
  replyToMessageId?: string;
  createdAt?: string;
  editedAt?: string;
  updatedAt?: string;
  updatedBy?: number;
}

const ENDPOINT = "/documents/chat-messages";

export const chatMessageDocumentService = {
  create: (data: ChatMessageDocument) =>
    api.post<ChatMessageDocument>(ENDPOINT, data),
  getAll: () => api.get<ChatMessageDocument[]>(ENDPOINT),
  getById: (id: string) =>
    api.get<ChatMessageDocument>(`${ENDPOINT}/${id}`),
  getByPublicId: (tenantId: string, publicId: string) =>
    api.get<ChatMessageDocument>(`${ENDPOINT}/public-id`, {
      params: { tenantId, publicId },
    }),
  getByChannelId: (channelId: string) =>
    api.get<ChatMessageDocument[]>(`${ENDPOINT}/channel/${channelId}`),
  getBySenderId: (senderId: number) =>
    api.get<ChatMessageDocument[]>(`${ENDPOINT}/sender/${senderId}`),
  getByTenantAndChannel: (tenantId: string, channelId: string) =>
    api.get<ChatMessageDocument[]>(`${ENDPOINT}/tenant-channel`, {
      params: { tenantId, channelId },
    }),
  update: (id: string, data: ChatMessageDocument) =>
    api.put<ChatMessageDocument>(`${ENDPOINT}/${id}`, data),
  delete: (id: string) => api.delete<void>(`${ENDPOINT}/${id}`),
};

