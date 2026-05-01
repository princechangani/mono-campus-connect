package com.monocampusconnect.model.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Map;

@Data
@Document(collection = "report_snapshots")
public class ReportSnapshotDocument {
    @Id
    private String id;

    @Field("public_id")
    private String publicId;

    @Field("tenant_id")
    private String tenantId;

    @Field("report_type")
    private String reportType;

    @Field("reference_type")
    private String referenceType;

    @Field("reference_id")
    private String referenceId;

    private Map<String, Object> parameters;
    private Map<String, Object> data;

    @Field("generated_at")
    private Date generatedAt;

    @Field("generated_by")
    private Long generatedBy;

    @Field("expires_at")
    private Date expiresAt;
}

