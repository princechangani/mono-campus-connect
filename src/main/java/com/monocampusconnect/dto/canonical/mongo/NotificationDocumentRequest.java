package com.monocampusconnect.dto.canonical.mongo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class NotificationDocumentRequest {
    @NotBlank
    private String tenantId;

    @NotBlank
    private String notificationType;

    @NotBlank
    private String category;

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    private Map<String, Object> data;
    private List<RecipientRequest> recipients;
    private String bulkGroup;
    private Date scheduledAt;
    private String status;

    @Data
    public static class RecipientRequest {
        private Long userId;
        private Date deliveredAt;
        private Date readAt;
        private Date failedAt;
        private String failureReason;
    }
}

