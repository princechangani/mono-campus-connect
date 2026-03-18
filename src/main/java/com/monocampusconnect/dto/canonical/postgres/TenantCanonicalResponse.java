package com.monocampusconnect.dto.canonical.postgres;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class TenantCanonicalResponse {
    private UUID tenantId;
    private UUID tenantPublicId;
    private String name;
    private String slug;
    private String domain;
    private String plan;
    private String timezone;
    private String locale;
    private String logoUrl;
    private String primaryColor;
    private Integer maxStudents;
    private Integer maxFaculty;
    private Boolean active;
    private OffsetDateTime subscriptionExpiresAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}

