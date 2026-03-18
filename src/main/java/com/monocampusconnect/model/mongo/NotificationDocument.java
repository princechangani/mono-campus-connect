package com.monocampusconnect.model.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "notifications")
@CompoundIndexes({
        @CompoundIndex(name = "idx_notification_tenant_deleted", def = "{'tenant_id': 1, 'is_deleted': 1}"),
        @CompoundIndex(name = "idx_notification_tenant_status_schedule", def = "{'tenant_id': 1, 'status': 1, 'scheduled_at': 1}")
})
public class NotificationDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field("public_id")
    private String publicId;

    @Field("tenant_id")
    private String tenantId;

    @Field("notification_type")
    private String notificationType;

    private String category;
    private String title;
    private String body;
    private Map<String, Object> data;
    private List<Recipient> recipients;

    @Field("bulk_group")
    private String bulkGroup;

    @Field("sent_at")
    private Date sentAt;

    @Field("scheduled_at")
    private Date scheduledAt;

    private String status;

    @Field("created_at")
    private Date createdAt;

    @Field("created_by")
    private Long createdBy;

    @Field("updated_at")
    private Date updatedAt;

    @Field("updated_by")
    private Long updatedBy;

    @Field("deleted_at")
    private Date deletedAt;

    @Field("deleted_by")
    private Long deletedBy;

    @Field("is_deleted")
    private boolean deleted = false;

    @Data
    public static class Recipient {
        @Field("user_id")
        private Long userId;

        @Field("delivered_at")
        private Date deliveredAt;

        @Field("read_at")
        private Date readAt;

        @Field("failed_at")
        private Date failedAt;

        @Field("failure_reason")
        private String failureReason;
    }
}
