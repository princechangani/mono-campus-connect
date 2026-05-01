package com.monocampusconnect.dto.mongo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MaterialDocumentResponse {
    private String id;
    private String publicId;
    private String tenantId;
    private Long courseAssignmentId;
    private Long courseId;
    private Long facultyId;
    private Long batchId;
    private Integer semesterNumber;
    private String title;
    private String description;
    private String materialType;
    private String fileUrl;
    private Long fileSizeBytes;
    private String mimeType;
    private String thumbnailUrl;
    private List<String> tags;
    private String visibility;
    private boolean active;
    private long viewCount;
    private long downloadCount;
    private Date createdAt;
    private Date updatedAt;
}

