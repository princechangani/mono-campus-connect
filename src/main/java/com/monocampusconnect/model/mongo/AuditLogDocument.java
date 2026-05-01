package com.monocampusconnect.model.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "audit_logs")
public class AuditLogDocument {

    @Id
    private String id;

    @Field("tenant_id")
    private String tenantId;

    @Field("actor_user_id")
    private Long actorUserId;

    @Field("actor_role")
    private String actorRole;

    private String action;

    @Field("resource_type")
    private String resourceType;

    @Field("resource_id")
    private String resourceId;

    @Field("resource_table")
    private String resourceTable;

    @Field("old_value")
    private Map<String, Object> oldValue;

    @Field("new_value")
    private Map<String, Object> newValue;

    private List<String> diff;

    @Field("ip_address")
    private String ipAddress;

    @Field("user_agent")
    private String userAgent;

    @Field("request_id")
    private String requestId;

    @Field("created_at")
    private Date createdAt;
}
