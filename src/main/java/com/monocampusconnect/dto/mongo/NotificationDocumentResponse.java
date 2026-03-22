package com.monocampusconnect.dto.mongo;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class NotificationDocumentResponse {
    private String id;
    private String publicId;
    private String tenantId;
    private String notificationType;
    private String category;
    private String title;
    private String body;
    private Map<String, Object> data;
    private List<RecipientResponse> recipients;
    private String bulkGroup;
    private Date sentAt;
    private Date scheduledAt;
    private String status;
    private Date createdAt;
    private Date updatedAt;

    @Data
    public static class RecipientResponse {
        private Long userId;
        private Date deliveredAt;
        private Date readAt;
        private Date failedAt;
        private String failureReason;
    }
}

