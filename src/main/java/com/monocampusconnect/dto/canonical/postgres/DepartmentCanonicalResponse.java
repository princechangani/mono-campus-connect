package com.monocampusconnect.dto.canonical.postgres;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class DepartmentCanonicalResponse {
    private Long departmentId;
    private UUID departmentPublicId;
    private UUID tenantId;
    private String name;
    private String code;
    private Long headFacultyId;
    private Long parentDepartmentId;
    private String description;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}

