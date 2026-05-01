package com.monocampusconnect.dto.mongo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class MaterialDocumentRequest {
    @NotBlank
    private String tenantId;

    private Long courseAssignmentId;

    @NotNull
    private Long courseId;

    @NotNull
    private Long facultyId;

    private Long batchId;
    private Integer semesterNumber;

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String materialType;

    private String fileUrl;
    private Long fileSizeBytes;
    private String mimeType;
    private String thumbnailUrl;
    private List<String> tags;

    @NotBlank
    private String visibility;

    private Boolean active;
}

