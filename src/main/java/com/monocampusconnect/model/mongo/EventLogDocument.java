package com.monocampusconnect.model.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Map;

@Data
@Document(collection = "event_logs")
@CompoundIndexes({
        @CompoundIndex(name = "idx_event_tenant_user", def = "{'tenant_id': 1, 'user_id': 1, 'created_at': -1}"),
        @CompoundIndex(name = "idx_event_tenant_name", def = "{'tenant_id': 1, 'event_name': 1, 'created_at': -1}")
})
public class EventLogDocument {

    @Id
    private String id;

    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("session_id")
    private String sessionId;

    @Field("user_id")
    private Long userId;

    @Field("event_name")
    private String eventName;

    private Map<String, Object> properties;

    private String device;
    private String os;
    private String browser;

    @Field("ip_address")
    private String ipAddress;

    @Indexed(expireAfter = "90d") // 90 days TTL
    @Field("created_at")
    private Instant createdAt;
}
