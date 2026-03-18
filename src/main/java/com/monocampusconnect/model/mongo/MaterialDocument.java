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

@Data
@Document(collection = "study_materials")
@CompoundIndexes({
        @CompoundIndex(name = "idx_material_tenant_deleted", def = "{'tenant_id': 1, 'is_deleted': 1}"),
        @CompoundIndex(name = "idx_material_tenant_course", def = "{'tenant_id': 1, 'course_id': 1, 'semester_number': 1, 'is_deleted': 1}")
})
public class MaterialDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field("public_id")
    private String publicId;

    @Field("tenant_id")
    private String tenantId;

    @Field("course_assignment_id")
    private Long courseAssignmentId;

    @Field("course_id")
    private Long courseId;

    @Field("faculty_id")
    private Long facultyId;

    @Field("batch_id")
    private Long batchId;

    @Field("semester_number")
    private Integer semesterNumber;

    private String title;
    private String description;

    @Field("material_type")
    private String materialType;

    @Field("file_url")
    private String fileUrl;

    @Field("file_size_bytes")
    private Long fileSizeBytes;

    @Field("mime_type")
    private String mimeType;

    @Field("thumbnail_url")
    private String thumbnailUrl;

    private List<String> tags;
    private String visibility;

    @Field("is_active")
    private boolean active = true;

    @Field("view_count")
    private long viewCount;

    @Field("download_count")
    private long downloadCount;

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
}
