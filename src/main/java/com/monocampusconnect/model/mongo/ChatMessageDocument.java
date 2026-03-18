package com.monocampusconnect.model.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "chat_messages")
public class ChatMessageDocument {
    @Id
    private String id;

    @Field("public_id")
    private String publicId;

    @Field("tenant_id")
    private String tenantId;

    @Field("channel_id")
    private String channelId;

    @Field("channel_type")
    private String channelType;

    @Field("sender_id")
    private Long senderId;

    @Field("message_type")
    private String messageType;

    private String text;
    private List<Map<String, Object>> attachments;

    @Field("reply_to_message_id")
    private String replyToMessageId;

    private List<Map<String, Object>> reactions;

    @Field("read_by")
    private List<Map<String, Object>> readBy;

    @Field("edited_at")
    private Date editedAt;

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
    private boolean deleted;
}

