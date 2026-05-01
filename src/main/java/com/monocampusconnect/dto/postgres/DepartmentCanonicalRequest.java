package com.monocampusconnect.dto.postgres;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class DepartmentCanonicalRequest {
    @NotNull
    private UUID tenantId;

    @NotBlank
    private String name;

    @NotBlank
    private String code;

    private Long headFacultyId;
    private Long parentDepartmentId;
    private String description;
}

